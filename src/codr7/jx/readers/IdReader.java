package codr7.jx.readers;

import codr7.jx.IForm;
import codr7.jx.Input;
import codr7.jx.Location;
import codr7.jx.Reader;
import codr7.jx.forms.IdForm;

import java.util.Deque;

public class IdReader implements Reader {
    public static final IdReader instance = new IdReader();

    public boolean read(final Input in, final Deque<IForm> out, final Location location) {
        final var loc = location.copy();
        final var buffer = new StringBuilder();

        for (; ; ) {
            var c = in.peek();

            if (c == 0 ||
                    Character.isWhitespace(c) ||
                    c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' || c == '.' || c == ':') {
                break;
            }

            in.pop();
            buffer.append(c);
            location.update(c);
        }

        if (buffer.isEmpty()) { return false; }
        out.addLast(new IdForm(buffer.toString(), loc));
        return true;
    }
}