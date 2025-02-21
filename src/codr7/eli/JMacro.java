package codr7.eli;

import codr7.eli.errors.EmitError;

public record JMacro(String id, Arg[] args, IType result, Body body) {
    public interface Body {
        void call(VM vm, IForm[] args, int rResult, Loc loc);
    }

    public int arity() {
        if (args.length > 0 && args[args.length-1].splat) { return -1; }
        var result = 0;

        for (final var a: args) {
            if (!a.opt) { result++; }
        }

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