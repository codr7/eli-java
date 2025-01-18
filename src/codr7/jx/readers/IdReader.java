package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.forms.IdForm;

import java.util.Deque;

public class IdReader implements Reader {
    public static final IdReader instance = new IdReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc location) {
        final var loc = location.dup();
        final var buffer = new StringBuilder();

        for (; ; ) {
            var c = in.peek();

            if (c == 0 || Character.isWhitespace(c) ||
                    c == '(' || c == ')' ||
                    c == '[' || c == ']' ||
                    c == '{' || c == '}' ||
                    c == '.' || c == ':' || c == '\'' || c == '"' || c == '#') { break; }

            in.pop();
            buffer.append(c);
            location.update(c);
            if (c == '^') { break; }
        }

        if (buffer.isEmpty()) { return false; }
        out.addLast(new IdForm(buffer.toString(), loc));
        return true;
    }
}