package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.forms.LiteralForm;
import codr7.eli.libs.CoreLib;

import java.math.BigDecimal;
import java.util.Deque;

public class DecimalReader implements Reader {
    public static final DecimalReader instance = new DecimalReader();

    public boolean read(final VM vm, final Input in, final long value, final Deque<IForm> out, final Loc loc) {
        var c = in.peek();
        var isNeg = value < 0;

        if (c == '-') {
            isNeg = true;
            in.pop();
            c = in.peek();
        }

        if (c != '.') {
            if (isNeg) { in.push('-'); }
            return false;
        }

        final var floc = loc.dup();
        loc.update(in.pop());
        var e = 0;
        var v = new StringBuilder();
        if (isNeg) { v.append('-'); }
        v.append(Math.abs(value));
        v.append('.');

        for (; ; ) {
            c = in.peek();
            if (!Character.isDigit(c)) { break; }
            loc.update(in.pop());
            v.append(c);
            e++;
        }

        out.addLast(new LiteralForm(new Value<>(CoreLib.decimalType, new BigDecimal(v.toString())), floc));
        return true;
    }

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        return read(vm, in, 0L, out, loc);
    }
}