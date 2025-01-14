package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.CoreLib;

import java.util.Deque;

public class IntReader implements Reader {
    public static final IntReader instance = new IntReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        var c = in.peek();
        if (!Character.isDigit(c)) { return false; }
        final var floc = loc.dup();
        var v = 0L;

        for (; ; ) {
            c = in.peek();

            if (c == '.') {
                return FixReader.instance.read(vm, in, v, out, loc);
            }

            if (!Character.isDigit(c)) { break; }
            in.pop();
            v = v * 10 + Character.digit(c, 10);
            loc.update(c);
        }

        out.addLast(new LiteralForm(new Value<>(CoreLib.intType, v), loc));
        return true;
    }
}