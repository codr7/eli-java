package codr7.eli;

import codr7.eli.libs.CoreLib;
import codr7.eli.ops.AddItem;
import codr7.eli.ops.MakeList;

import java.util.ArrayList;
import java.util.Arrays;

public final class Arg {
    String id;
    IType type;

    boolean opt = false;
    boolean quote = false;
    boolean splat = false;

    public Arg(String id, final IType type) {
        if (id.charAt(0) == '\'') {
            quote = true;
            id = id.substring(1);
        }

        var done = false;

        while (!done) {
            switch (id.charAt(id.length() - 1)) {
                case '*':
                    splat = true;
                    id = id.substring(0, id.length() - 1);
                    break;
                case '?':
                    opt = true;
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

    public Arg(final String id) { this(id, CoreLib.anyType); }

    public int bind(final VM vm, final IValue[] values, final int i, final int rResult, final Loc loc) {
        if (splat) {
            final var vs = new ArrayList<>(Arrays.asList(values).subList(i, values.length));
            vm.registers.set(rResult, new Value<>(CoreLib.listType, vs));
            return values.length;
        }

        vm.registers.set(rResult, values[i]);
        return i+1;
    }

    public String dump(final VM vm) {
        var s = id;
        if (quote) { s = '\'' + s; }
        if (splat) { s = s + '*'; }
        if (opt) { s = s + '?'; }
        return s;
    }
}
