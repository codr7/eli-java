package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.CoreLib;

import java.util.Deque;

public class FixReader implements Reader {
    public static final FixReader instance = new FixReader();

    public boolean read(final VM vm, final Input in, final long value, final Deque<IForm> out, final Loc loc) {
        var c = in.peek();
        if (c != '.') { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        var e = 0;
        var v = value;

        for (; ; ) {
            c = in.peek();
            if (!Character.isDigit(c)) { break; }
            loc.update(in.pop());
            v = v * 10 + Character.digit(c, 10);
            e++;
        }

        System.out.println("FIX " + e + " " + v + " " + Fix.make(e, v));
        out.addLast(new LiteralForm(new Value<>(CoreLib.fixType, Fix.make(e, v)), floc));
        return true;
    }

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        return read(vm, in, 0L, out, loc);
    }
}