package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;

import java.util.Deque;

public abstract class PrefixReader implements Reader {
    public final char prefix;

    protected PrefixReader(final char prefix) {
        this.prefix = prefix;
    }

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != prefix) {
            return false;
        }
        final var floc = loc.dup();
        loc.update(in.pop());
        if (!vm.read(in, out, loc)) {
            throw new ReadError("Missing target", loc);
        }
        out.addLast(boxTarget(out.removeLast(), floc));
        return true;
    }

    protected abstract IForm boxTarget(IForm target, Loc loc);
}