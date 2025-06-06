package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.forms.LiteralForm;
import codr7.eli.libs.CoreLib;

import java.util.Deque;

public class IntReader implements Reader {
    public static final IntReader instance = new IntReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        var c = in.peek();
        var isNeg = false;

        if (c == '-') {
            isNeg = true;
            in.pop();
            c = in.peek();
        }

        if (!Character.isDigit(c)) {

            if (isNeg) {
                in.push('-');
            }

            return false;
        }

        final var floc = loc.dup();
        var v = 0L;

        for (; ; ) {
            c = in.peek();

            if (c == '.') {
                return DecimalReader.instance.read(vm, in, isNeg ? -v : v, out, loc);
            }

            if (!Character.isDigit(c)) {
                break;
            }
            in.pop();
            v = v * 10 + Character.digit(c, 10);
            loc.update(c);
        }

        out.addLast(new LiteralForm(new Value<>(CoreLib.Int, isNeg ? -v : v), loc));
        return true;
    }
}