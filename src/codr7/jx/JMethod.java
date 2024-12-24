package codr7.jx;

import codr7.jx.errors.EmitError;

public record JMethod(String id, Arg[] args, IType result, Body body) {
    public interface Body {
        void call(VM vm, IValue[] args, int rResult, Loc loc);
    }

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].id().endsWith("*")) { return -1; }
        return result;
    }

    public void call(final VM vm,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        final var a = arity();
        if (a != -1 && arity < a) { throw new EmitError("Not enough args: " + this, loc); }
        final var args = new IValue[arity];
        for (var i = 0; i < arity ; i++) { args[i] = vm.registers.get(rArgs + i); }
        body.call(vm, args, rResult, loc);
    }

}