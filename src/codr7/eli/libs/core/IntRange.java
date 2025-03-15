package codr7.eli.libs.core;

import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.Value;
import codr7.eli.libs.CoreLib;

public final class IntRange implements Iter {
    private final long end;
    private final long stride;
    private long current;

    public IntRange(final long start, final long end, final long stride) {
        this.current = start;
        this.end = end;
        this.stride = stride;
    }

    @Override
    public String dump(final VM vm) {
        return "(IntRange " + current + ".." + end + ":" + stride + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (current >= end) {
            return false;
        }
        if (rResult != -1) {
            vm.registers.set(rResult, new Value<>(CoreLib.Int, current));
        }
        current += stride;
        return true;
    }
}
