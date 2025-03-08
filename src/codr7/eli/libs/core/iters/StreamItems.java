package codr7.eli.libs.core.iters;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;

import java.util.Iterator;
import java.util.stream.Stream;

public final class StreamItems implements Iter {
    private final Iterator<IValue> in;

    public StreamItems(final Stream<IValue> in) {
        this.in = in.iterator();
    }

    @Override
    public String dump(final VM vm) {
        return "(StreamItems " + in + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (!in.hasNext()) {
            return false;
        }
        final var v = in.next();

        if (rResult != -1) {
            vm.registers.set(rResult, v);
        }

        return true;
    }
}