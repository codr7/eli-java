package codr7.eli;

import codr7.eli.errors.EmitError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.Arrays;

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
        var args = new IValue[arity];
        var ai = 0;

        for (var i = 0; i < arity; ai++, i++) {
            final var v = vm.registers.get(rArgs + ai);

            if (v.type() == CoreLib.splatType) {
                final var svs = new ArrayList<IValue>();
                final var it = v.cast(CoreLib.splatType);

                while (it.next(vm, vm.rScratch, loc)) {
                    svs.add(vm.registers.get(vm.rScratch));
                }

                args = Arrays.copyOf(args, args.length + svs.size() - 1);

                for (final var av: svs) {
                    args[i] = av;
                    i++;
                }
            } else {
                args[i] = v;
            }
        }

        if (arity() != -1 && args.length < arity()) {
            throw new EmitError("Not enough args: " + this, loc);
        }

        body.call(vm, args, rResult, loc);
    }
}