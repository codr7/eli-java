package codr7.eli.libs.iter.iters;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.traits.CallableTrait;

public final class MapResult implements Iter {
    private final Iter[] in;
    private final IValue[] inValues;
    private final CallableTrait callType;
    private final IValue callTarget;
    private final int rIn;

    @Override
    public String dump(final VM vm) {
        return "(iter/MapResult)";
    }

    public MapResult(final VM vm, final Iter[] in, final IValue callback, final Loc loc) {
        this.in = in;
        inValues = new IValue[in.length];
        callType = callback.type().cast(CoreLib.Callable, loc);
        callTarget = callback;
        rIn = vm.alloc(1);
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        for (var i = 0; i < in.length; i++) {
            if (in[i].next(vm, rIn, loc)) {
                inValues[i] = vm.registers.get(rIn);
            } else {
                return false;
            }
        }

        callType.call(vm, callTarget, inValues, (rResult == -1) ? vm.rNull : rResult, true, loc);
        return true;
    }
}