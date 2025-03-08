package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.IValue;
import codr7.eli.Lib;
import codr7.eli.Value;

import java.util.ArrayList;
import java.util.Arrays;

public final class ListLib extends Lib {
    public ListLib() {
        super("list", null);

        bindMethod("alloc",
                new Arg[]{new Arg("n", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                    final var vs = new IValue[args[0].cast(CoreLib.Int).intValue()];
                    Arrays.fill(vs, CoreLib.NIL);
                    vm.registers.set(rResult, new Value<>(CoreLib.List, new ArrayList<>(Arrays.asList(vs))));
                });

        bindMethod("into", new Arg[]{new Arg("in", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    final var it = in.type().cast(CoreLib.Iterable, loc);
                    final var rValue = vm.alloc(1);
                    final var out = new ArrayList<IValue>();

                    for (final var i = it.iter(vm, in); i.next(vm, rValue, loc); ) {
                        out.add(vm.registers.get(rValue));
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.List, out));
                });

        bindMethod("push",
                new Arg[]{new Arg("list", CoreLib.List), new Arg("value")},
                (vm, args, rResult, loc) -> {
                    args[0].cast(CoreLib.List).add(args[1]);
                });
    }
}