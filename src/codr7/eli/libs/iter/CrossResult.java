package codr7.eli.libs.iter;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.CallableTrait;
import codr7.eli.libs.core.StreamItems;

import java.util.ArrayList;
import java.util.Iterator;

public final class CrossResult implements Iter {
    private final Iter xi;
    private final IValue ys;
    private ArrayList<IValue> yvs = null;
    private Iterator<IValue> yi = null;
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
        final var ok = xi.next(vm, rArgs, loc);

        if (ok) {
            if (yvs == null) {
                yvs = new ArrayList<>();
                final var it = ys.type().cast(CoreLib.Iterable, loc).iter(vm, ys);

                while (it.next(vm, rArgs+1, loc)) {
                    yvs.add(vm.registers.get(rArgs+1));
                }
            }

            yi = yvs.iterator();
        }

        return ok;
    }

    private boolean nextY(final VM vm, final Loc loc) {
        if (yi.hasNext()) {
            vm.registers.set(rArgs + 1, yi.next());
            return true;
        }

        return false;
    }

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
        }

        if (!nextY(vm, loc)) {
            yi = null;
            return next(vm, rResult, loc);
        }

        callType.call(vm, callTarget, rArgs, 2, (rResult == -1) ? vm.rNull : rResult, true, loc);
        return true;
    }
}