package codr7.eli;

import codr7.eli.errors.EvalError;

import java.util.ArrayList;
import java.util.Collections;

public final class Dispatch {
    public final String id;
    private final ArrayList<IMethod> methods = new ArrayList<>();

    public Dispatch(final String id) {
        this.id = id;
    }

    public void add(final IMethod m) {
        var i = Collections.binarySearch(methods, m);
        if (i < 0) { i = Math.abs(i + 1); }

        if (i == methods.size()) {
            methods.add(m);
        } else {
            methods.add(i, m);
        }
    }

    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        final var m = findMethod(vm, args);

        if (m == null) {
            throw new EvalError("No applicable methods found", loc);
        }

        m.call(vm, args, rResult, eval, loc);
    }

    public String dump(final VM vm) {
        return "(^" + id + " [" + methods.size() + ']';
    }

    public IMethod findMethod(final VM vm, final IValue[] args) {
        var w = 0;

        for (final var v: args) {
            w += v.type().weight();
        }

        var min = 0;
        var max = methods.size();

        while (min < max) {
            var i = (max - min) / 2;
            final var m = methods.get(i);

            if (args.length < m.minArity()) {
                max = i;
            } else if (args.length > m.maxArity()) {
                min = i;
            } else if (w < m.weight()) {
                max = i;
            } else {
                return m;
            }
        }

        return null;
    }
}
