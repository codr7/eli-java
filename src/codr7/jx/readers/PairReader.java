package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.PairForm;

import java.util.Deque;

public class PairReader implements Reader {
    public static final PairReader instance = new PairReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Location location) {
        if (in.peek() != ':') { return false; }
        final var loc = location.dup();
        location.update(in.pop());
        if (out.isEmpty() || !vm.read(in, out, location)) { throw new ReadError("Invalid pair", location); }
        final var right = out.removeLast();
        final var left = out.removeLast();
        out.addLast(new PairForm(left, right, loc));
        return true;
    }
}