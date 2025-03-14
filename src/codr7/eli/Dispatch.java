package codr7.eli;

import codr7.eli.errors.EvalError;

import java.util.ArrayList;

public final class Dispatch {
    public final String id;
    private final ArrayList<IMethod> methods = new ArrayList<>();

    public Dispatch(final String id) {
        this.id = id;
    }

    public void add(final IMethod m) {
       methods.addFirst(m);
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
        return "(^" + id + " [" + methods.size() + ']';
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
