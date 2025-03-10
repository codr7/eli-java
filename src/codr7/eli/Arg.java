package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.Arrays;

public final class Arg {
    String id;
    IType type;

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
        this(id, CoreLib.Any);
    }

    public int bind(final VM vm, final IValue[] values, final int i, final int rResult, final Loc loc) {
        if (splat) {
            final var vs = new ArrayList<>(Arrays.asList(values).subList(i, values.length));
            vm.registers.set(rResult, new Value<>(CoreLib.List, vs));
            return values.length;
        }

        if (values.length > i) {
            vm.registers.set(rResult, values[i]);
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
}
