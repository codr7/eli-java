package codr7.eli;

import codr7.eli.errors.EmitError;

public final class JMethod extends BaseMethod implements IMethod {
    public final Body body;

    public JMethod(final String id, final Arg[] args, final Body body) {
        super(id, args);
        this.body = body;
    }

    @Override
    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     boolean eval,
                     final Loc loc) {
        if (args.length < minArity || args.length > maxArity) {
            throw new EmitError("Wrong number of args: " + this, loc);
        }

        body.call(vm, args, rResult, loc);
    }

    public interface Body {
        void call(VM vm, IValue[] args, int rResult, Loc loc);
    }
}