package codr7.eli;

import codr7.eli.libs.CoreLib;
import codr7.eli.ops.AddItem;
import codr7.eli.ops.List;

import java.util.ArrayList;

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

    public Arg(final String id) { this(id, null); }

    public int bind(final VM vm, final IForm[] args, final int i, final int rResult, final Loc loc) {
        if (quote) {
            if (splat) {
                final var vs = new ArrayList<IValue>();

                for (var j = i; j < args.length; j++) {
                    final var af = args[j];
                    vs.add(new Value<>(CoreLib.exprType, af));
                }

                vm.currentLib.bind(id, new Value<>(CoreLib.listType, vs));
                return args.length;
            }

            final var af = args[i];
            vm.currentLib.bind(id, new Value<>(CoreLib.exprType, af));
            return i+1;
        }

        if (splat) {
            vm.emit(new List(rResult));
            final var rItem = vm.alloc(1);

            for (var j = i; j < args.length; j++) {
                args[j].emit(vm, rItem);
                vm.emit(new AddItem(rResult, rItem, loc));
            }

            return args.length;
        }

        args[i].emit(vm, rResult);
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
