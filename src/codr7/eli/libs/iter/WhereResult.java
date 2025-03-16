package codr7.eli.libs.iter;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.CallableTrait;

import java.util.Arrays;

public final class WhereResult implements Iter {
    private final Iter[] args;
    private final IValue[] argValues;
    private final CallableTrait predType;
    private final IValue pred;
    private final int rArg;
    private final int rPred;

    public WhereResult(final VM vm, final Iter[] args, final IValue pred, final Loc loc) {
        this.args = args;
        argValues = new IValue[args.length];
        this.pred = pred;
        predType = pred.type().cast(CoreLib.Callable, loc);
        rPred = vm.alloc(1);
        rArg = vm.alloc(1);
    }

    @Override
    public String dump(final VM vm) {
        return "(iter/WhereResult #" + rArg + ' ' + args.length + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        for (var i = 0; i < args.length; i++) {
            if (!args[i].next(vm, rArg, loc)) {
                return false;
            }

            argValues[i] = vm.registers.get(rArg);
        }

        predType.call(vm, pred, argValues, rPred, true, loc);

        if (vm.registers.get(rPred).cast(CoreLib.Bit)) {
            if (rResult != -1) {
                vm.registers.set(rResult, Value.zip(Arrays.stream(argValues).iterator()));
            }

            return true;
        }

        return next(vm, rResult, loc);
    }
}