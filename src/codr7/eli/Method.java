package codr7.eli;

import codr7.eli.errors.EvalError;

import static codr7.eli.libs.CoreLib.bindingType;

public record Method(String id,
                     Arg[] args, int rArgs,
                     int rResult,
                     Label start, Label end,
                     Loc loc) {

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].splat) { return -1; }
        return result;
    }

    public void bindArgs(final VM vm) {
        for (var i = 0; i < args.length; i++) {
            final var ma = args[i];
            vm.currentLib.bind(ma.id, bindingType, new Binding(null, rArgs + i));
        }
    }

    public void call(final VM vm,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        if (arity() != -1 && arity < arity()) {
            throw new EvalError("Not enough args: " + dump(vm), loc);
        }

        final var avs = new IValue[arity];

        for (var i = 0; i < arity; i++) {
            avs[i] = vm.registers.get(rArgs + i);
        }

        var rArg = this.rArgs;
        var ai = 0;

        for (final var a: args) {
            ai = a.bind(vm, avs, ai, rArg, loc);
            rArg++;
        }

        vm.beginCall(this, vm.pc, rResult, loc);
        vm.pc = start.pc;
    }

    public String dump(final VM vm) {
        return "(Method " + id + ")";
    }
}
