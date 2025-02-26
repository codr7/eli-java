package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;
import codr7.eli.forms.LiteralForm;
import codr7.eli.forms.PairForm;
import codr7.eli.libs.CoreLib;

import java.util.Deque;
import java.util.TreeMap;

public class MapReader implements Reader {
    public static final MapReader instance = new MapReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc location) {
        if (in.peek() != '{') { return false; }
        final var loc = location.dup();
        location.update(in.pop());
        final var m = new TreeMap<IValue, IValue>();


        for (; ; ) {
            WhitespaceReader.instance.read(vm, in, out, location);
            var c = in.peek();
            if (c == 0) { throw new ReadError("Unexpected end of map", location); }

            if (c == '}') {
                in.pop();
                break;
            }

            if (!PairReader.instance.read(vm, in, out, location)) { throw new ReadError("Unexpected end of map", location); }
            final var pf = (PairForm)out.removeLast();
            m.put(pf.left.value(vm), pf.right.value(vm));
        }

        out.addLast(new LiteralForm(new Value<>(CoreLib.mapType, m), loc));
        return true;
    }
}