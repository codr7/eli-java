package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;
import codr7.eli.forms.LiteralForm;
import codr7.eli.libs.CoreLib;

import java.util.Deque;

public class StringReader implements Reader {
    public static final StringReader instance = new StringReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '"') {
            return false;
        }
        final var floc = loc.dup();
        loc.update(in.pop());
        final var buffer = new StringBuilder();

        for (; ; ) {
            var c = in.peek();

            if (c == 0) {
                throw new ReadError("Unexpected end of string", loc);
            }

            loc.update(in.pop());

            if (c == '"') {
                break;
            }

            if (c == '\\') {
                c = in.pop();

                switch (c) {
                    case '"':
                    case '\\':
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    default:
                        throw new ReadError("Invalid escape: " + c, loc);
                }
            }

            buffer.append(c);
        }

        out.addLast(new LiteralForm(new Value<>(CoreLib.String, buffer.toString()), floc));
        return true;
    }
}