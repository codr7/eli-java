package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.QuoteForm;

import java.util.Deque;

public abstract class PrefixReader implements Reader {
    public final char prefix;

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != prefix) { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        if (!vm.read(in, out, loc)) { throw new ReadError("Missing target", loc); }
        out.addLast(boxTarget(out.removeLast(), floc));
        return true;
    }

    protected PrefixReader(final char prefix) {
        this.prefix = prefix;
    }

    protected abstract IForm boxTarget(IForm target, Loc loc);
}