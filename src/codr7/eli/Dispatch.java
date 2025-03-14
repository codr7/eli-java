package codr7.eli;

import codr7.eli.errors.EvalError;

public final class Dispatch {
    public final String id;
    public final IMethod[] methods;

    public Dispatch(final String id, IMethod...methods) {
        this.id = id;
        this.methods = methods;
    }

    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        final var m = findMethod(args);

        if (m == null) {
            throw new EvalError("No applicable methods found", loc);
        }

        m.call(vm, args, rResult, eval, loc);
    }

    public String dump(final VM vm) {
        return "(^" + id + " [" + methods.length + ']';
    }

    public IMethod findMethod(final IValue[] args) {
        for (final var m: methods) {
            if (args.length >= m.minArity() && args.length <= m.maxArity()) {
                var i = 0;

                for (final var a: m.args()) {
                    i = a.check(args, i);

                    if (i == -1) {
                        break;
                    }
                }

                if (i != -1) {
                    return m;
                }
            }
        }

        return null;
    }
}
