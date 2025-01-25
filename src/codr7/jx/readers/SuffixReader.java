package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;

import java.util.Deque;

public abstract class SuffixReader implements Reader {
    public final char suffix;

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != suffix) { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        if (out.isEmpty()) { throw new ReadError("Missing target", loc); }
        out.addLast(boxTarget(out.removeLast(), floc));
        return true;
    }

    protected SuffixReader(final char suffix) {
        this.suffix = suffix;
    }

    protected abstract IForm boxTarget(IForm target, Loc loc);
}