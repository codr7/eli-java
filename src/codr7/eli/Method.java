package codr7.eli;

import codr7.eli.errors.EvalError;

import static codr7.eli.libs.CoreLib.Binding;

public record Method(String id,
                     Arg[] args, int rArgs,
                     int rResult,
                     Label start, Label end,
                     Loc loc) {

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result - 1].splat) {
            return -1;
        }
        return result;
    }

    public void bindArgs(final VM vm) {
        for (var i = 0; i < args.length; i++) {
            final var ma = args[i];
            vm.currentLib.bind(ma.id, Binding, new Binding(null, rArgs + i));
        }
    }

    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        if (arity() != -1 && args.length < arity()) {
            throw new EvalError("Not enough args: " + dump(vm), loc);
        }

        var rArg = this.rArgs;
        var ai = 0;

        for (final var a : this.args) {
            ai = a.bind(vm, args, ai, rArg, loc);
            rArg++;
        }

        if (eval) {
            vm.eval(start.pc, end.pc);

            if (rResult != this.rResult) {
                vm.registers.set(rResult, vm.registers.get(this.rResult()));
            }
        } else {
            vm.beginCall(this, vm.pc, rResult, loc);
            vm.pc = start.pc;
        }
    }

    public String dump(final VM vm) {
        return "(Method " + id + ")";
    }
}
