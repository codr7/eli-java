package codr7.jx;

import codr7.jx.errors.EmitError;

public record JMacro(String id, Arg[] args, IType result, Body body) {
    public interface Body {
        void call(VM vm, IForm[] args, int rResult, Loc loc);
    }

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].id().endsWith("*")) { return -1; }
        return result;
    }

    public void emit(final VM vm,
                     final IForm[] args,
                     final int rResult,
                     final Loc loc) {
        final var arity = arity();
        if (arity != -1 && args.length < arity) { throw new EmitError("Not enough args: " + this, loc); }
        body.call(vm, args, rResult, loc);
    }

    public String toString() { return String.format("(JMacro %s)", id); }
}