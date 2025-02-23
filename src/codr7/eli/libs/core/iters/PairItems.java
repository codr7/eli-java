package codr7.eli.libs.core.iters;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

public final class PairItems implements Iter {
    private IValue head;

    public PairItems(final IValue head) {
        this.head = head;
    }

    @Override
    public String dump(final VM vm) {
        return "(PairItems " + head.dump(vm) + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (head == null) { return false; }
        IValue v;

        if (head.type() == CoreLib.pairType) {
            final var p = head.cast(CoreLib.pairType);
            v = p.left();
            head = p.right();
        } else {
            v = head;
            head = null;
        }

        if (rResult != -1) { vm.registers.set(rResult, v); }
        return true;
    }
}