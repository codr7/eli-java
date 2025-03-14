package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.Arrays;

public final class Arg {
    public final String id;
    public final IType type;
    public final boolean optional;
    public final boolean splat;

    public Arg(String id, final IType type) {
        final var lc = id.charAt(id.length()-1);
        splat = lc == '*';
        optional = lc == '?';
        this.id = (splat || optional) ? id.substring(0, id.length() - 1) : id;
        this.type = type;
    }

    public Arg(final String id) {
        this(id, null);
    }

    public static int minArity(final Arg[] args) {
        final var l = args.length;

        var n = 0;

        for (final var a : args) {
            if (!a.optional && !a.splat) {
                n++;
            }
        }

        return n;
    }

    public static int maxArity(final Arg[] args) {
        final var l = args.length;

        if (l > 0 && args[l - 1].splat) {
            return Integer.MAX_VALUE;
        }

        return l;
    }

    public static int weight(final Arg[] args) {
        var w = 0;

        for (final var a : args) {
            w += a.weight();
        }

        return w;
    }

    public int bind(final VM vm, final IValue[] values, final int i, final int rResult, final Loc loc) {
        if (splat) {
            final var vs = new ArrayList<>(Arrays.asList(values).subList(i, values.length));

            if (type != null) {
                for (final var v : vs) {
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
            if (!optional) {
                throw new EvalError("Missing arg: " + id, loc);
            }

            vm.registers.set(rResult, CoreLib.NIL);
        }

        return i + 1;
    }

    public int check(final IValue[] values, int i) {
        if (splat) {
            if (type != null) {
                while (i < values.length) {
                    if (!values[i].type().isa(type)) {
                        return -1;
                    }

                    i++;
                }
            }

            return values.length;
        }

        return (values.length > i && (type != null && !values[i].type().isa(type))) ? -1 : i + 1;
    }

    public String dump(final VM vm) {
        var s = new StringBuilder();
        s.append(id);

        if (splat) {
            s.append('*');
        }

        if (optional) {
            s.append('?');
        }

        if (type != null) {
            s.append('@').append(type.id());
        }

        return s.toString();
    }

    public int weight() {
        return (type == null) ? 0 : type.weight();
    }
}
