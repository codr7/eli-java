package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.Arrays;

public final class Arg {
    public static int minArity(final Arg[] args) {
        final var l = args.length;

        var n = 0;

        for (final var a: args) {
            if (!a.optional && !a.splat) {
                n++;
            }
        }

        return n;
    }

    public static int maxArity(final Arg[] args) {
        final var l = args.length;

        if (l > 0 && args[l-1].splat) {
            return Integer.MAX_VALUE;
        }

        return l;
    }

    public static int weight(final Arg[] args) {
        var w = 0;

        for (final var a: args) {
            w += a.weight();
        }

        return w;
    }

    public final String id;
    public final IType type;

    boolean optional = false;
    boolean splat = false;

    public Arg(String id, final IType type) {
        var done = false;

        while (!done) {
            switch (id.charAt(id.length() - 1)) {
                case '*':
                    splat = true;
                    id = id.substring(0, id.length() - 1);
                    break;
                case '?':
                    optional = true;
                    id = id.substring(0, id.length() - 1);
                    break;
                default:
                    done = true;
                    break;
            }
        }

        this.id = id;
        this.type = type;
    }

    public Arg(final String id) {
        this(id, null);
    }

    public int bind(final VM vm, final IValue[] values, final int i, final int rResult, final Loc loc) {
        if (splat) {
            final var vs = new ArrayList<>(Arrays.asList(values).subList(i, values.length));

            if (type != null) {
                for (final var v: vs) {
                    v.typeCheck(vm, type, loc);
                }
            }

            vm.registers.set(rResult, new Value<>(CoreLib.List, vs));
            return values.length;
        }

        if (values.length > i) {
            final var v = values[i];

            if (type != null) {
                v.typeCheck(vm, type, loc);
            }

            vm.registers.set(rResult, v);
        } else {
            if (!optional){
                throw new EvalError("Missing arg: " + id, loc);
            }

            vm.registers.set(rResult, CoreLib.NIL);
        }

        return i + 1;
    }

    public String dump(final VM vm) {
        var s = id;
        if (splat) {
            s = s + '*';
        }
        if (optional) {
            s = s + '?';
        }
        return s;
    }

    public int weight() {
        return (type == null) ? 0 : type.weight();
    }
}
