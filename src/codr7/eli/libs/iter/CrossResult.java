package codr7.eli.libs.iter;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.CallableTrait;

public final class CrossResult implements Iter {
    private final Iter xi;
    private final IValue ys;
    private Iter yi;
    private final CallableTrait callType;
    private final IValue callTarget;
    private final int rArgs;

    public CrossResult(final VM vm, final IValue xs, final IValue ys, final IValue callback, final Loc loc) {
        xi = xs.type().cast(CoreLib.Iterable, loc).iter(vm, xs);
        this.ys = ys;
        callType = callback.type().cast(CoreLib.Callable, loc);
        callTarget = callback;
        rArgs = vm.alloc(2);
    }

    private boolean nextX(final VM vm, final Loc loc) {
        return xi.next(vm, rArgs, loc);
    }

    private void initY(final VM vm, final Loc loc) {
        yi = ys.type().cast(CoreLib.Iterable, loc).iter(vm, ys);
    }

    private boolean nextY(final VM vm, final Loc loc) {
        return yi.next(vm, rArgs + 1, loc);
    };

    @Override
    public String dump(final VM vm) {
        return "(iter/CrossResult #" + rArgs + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (yi == null) {
            if (!nextX(vm, loc)) {
                return false;
            }

            initY(vm, loc);
        }

        if (!nextY(vm, loc)) {
            yi = null;
            return next(vm, rResult, loc);
        }

        callType.call(vm, callTarget, rArgs, 2, (rResult == -1) ? vm.rNull : rResult, true, loc);
        return true;
    }
}