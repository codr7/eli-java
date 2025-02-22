package codr7.eli;

import codr7.eli.errors.EmitError;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.CallValue;

import java.util.ArrayList;

public record JMethod(String id, Arg[] args, IType result, Body body) {
    public interface Body {
        void call(VM vm, IValue[] args, int rResult, Loc loc);
    }

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].splat) { return -1; }
        return result;
    }

    public void call(final VM vm,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        final var args = new ArrayList<IValue>();

        for (var i = 0; i < arity ; i++) {
            final var v = vm.registers.get(rArgs + i);

            if (v.type() == CoreLib.splatType) {
                final var it = v.cast(CoreLib.splatType);

                while (it.next(vm, vm.rScratch, loc)) {
                    args.add(vm.registers.get(vm.rScratch));
                }
            } else {
                args.add(v);
            }
        }

        if (arity() != -1 && args.size() < arity()) {
            throw new EmitError("Not enough args: " + this, loc);
        }

        body.call(vm, args.toArray(new IValue[0]), rResult, loc);
    }
}