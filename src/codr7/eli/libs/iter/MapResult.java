package codr7.eli.libs.iter;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.CallableTrait;

public final class MapResult implements Iter {
    private final Iter[] args;
    private final CallableTrait callType;
    private final IValue callTarget;
    private final int rArgs;

    public MapResult(final VM vm, final Iter[] args, final IValue callback, final Loc loc) {
        this.args = args;
        callType = callback.type().cast(CoreLib.Callable, loc);
        callTarget = callback;
        rArgs = vm.alloc(args.length);
    }

    @Override
    public String dump(final VM vm) {
        return "(iter/MapResult #" + rArgs + ' ' + args.length + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        for (var i = 0; i < args.length; i++) {
            if (!args[i].next(vm, rArgs + i, loc)) {
                return false;
            }
        }

        callType.call(vm, callTarget, rArgs, args.length, (rResult == -1) ? vm.rNull : rResult, true, loc);
        return true;
    }
}