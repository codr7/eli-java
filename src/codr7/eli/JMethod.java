package codr7.eli;

import codr7.eli.errors.EmitError;

public record JMethod(String id, Arg[] args, Body body) {
    public int arity() {
        var result = args.length;
        if (result > 0 && args[result - 1].splat) {
            return -1;
        }
        return result;
    }

    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     final Loc loc) {
        if (arity() != -1 && args.length < arity()) {
            throw new EmitError("Not enough args: " + this, loc);
        }

        body.call(vm, args, rResult, loc);
    }

    public interface Body {
        void call(VM vm, IValue[] args, int rResult, Loc loc);
    }
}