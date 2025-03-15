package codr7.eli.libs.iter;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.CallableTrait;

public final class WhereResult implements Iter {
    private final Iter[] args;
    private final CallableTrait predType;
    private final IValue pred;
    private final int rArgs;
    private final int rPred;

    @Override
    public String dump(final VM vm) {
        return "(iter/WhereResult #" + rArgs + ' ' + args.length + ")";
    }

    public WhereResult(final VM vm, final Iter[] args, final IValue pred, final Loc loc) {
        this.args = args;
        this.pred = pred;
        predType = pred.type().cast(CoreLib.Callable, loc);
        rPred = vm.alloc(1);
        rArgs = vm.alloc(args.length);
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        for (var i = 0; i < args.length; i++) {
            if (!args[i].next(vm, rArgs+i, loc)) {
                return false;
            }
        }

        predType.call(vm, pred, rArgs, args.length, rPred,true, loc);

        if (vm.registers.get(rPred).cast(CoreLib.Bit)) {
            if (rResult != -1) {
                var v = vm.registers.get(rArgs);

                for (var i = 1; i < args.length; i++) {
                    v = new Value<>(CoreLib.Pair, new Pair(v, vm.registers.get(rArgs+i)));
                }

                vm.registers.set(rResult, v);
            }

            return true;
        }

        return next(vm, rResult, loc);
    }
}