package codr7.eli;

import codr7.eli.errors.EmitError;

public final class JMethod implements IMethod {
    public final String id;
    public final Arg[] args;
    public final Body body;
    public final int minArity;
    public final int maxArity;
    public final int weight;

    public JMethod(final String id, final Arg[] args, final Body body) {
        this.id = id;
        this.args = args;
        this.body = body;
        this.minArity = Arg.minArity(args);
        this.maxArity = Arg.maxArity(args);
        this.weight = Arg.weight(args);
    }

    @Override
    public Arg[] args() {
        return args;
    }

    @Override
    public int minArity() {
        return minArity;
    }

    @Override
    public int maxArity() {
        return maxArity;
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

    @Override
    public String dump(final VM vm) {
        return "(^" + id + "[?])";
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int weight() {
        return weight;
    }

    public interface Body {
        void call(VM vm, IValue[] args, int rResult, Loc loc);
    }
}