package codr7.jx.libs.core.iters;

import codr7.jx.Iter;
import codr7.jx.Loc;
import codr7.jx.VM;
import codr7.jx.Value;
import codr7.jx.libs.CoreLib;

public class IntRange implements Iter {
    private long current;
    private long end;
    private long stride;

    public IntRange(final long start, final long end, final long stride) {
        this.current = start;
        this.end = end;
        this.stride = stride;
    }

    @Override public String dump(VM vm) {
        return "(IntRange " + current + ":" + end + ":" + stride + ")";
    }

    @Override public boolean next(final VM vm, final int rResult, final Loc loc) {
        final var next = current + stride;
        if (next >= end) { return false; }
        current = next;
        if (rResult != -1) { vm.registers.set(rResult, new Value<>(CoreLib.intType, current)); }
        return true;
    }
}
