package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.libs.core.iters.StreamItems;
import codr7.eli.libs.core.traits.IterTrait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public final class ListLib extends Lib {
    public ListLib() {
        super("list", null);

        bindMethod("alloc",
                new Arg[]{new Arg("n", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var vs = new IValue[args[0].cast(CoreLib.intType).intValue()];
                    Arrays.fill(vs, CoreLib.NIL);
                    vm.registers.set(rResult, new Value<>(CoreLib.listType, new ArrayList<>(Arrays.asList(vs))));
                });

        bindMethod("into", new Arg[]{new Arg("in", CoreLib.iterTrait)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    final var it = in.type().cast(CoreLib.iterTrait, loc);
                    final var rValue = vm.alloc(1);
                    final var out = new ArrayList<IValue>();

                    for (final var i = it.iter(vm, in); i.next(vm, rValue, loc);) {
                        out.add(vm.registers.get(rValue));
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.listType, out));
                });

        bindMethod("push",
                new Arg[]{new Arg("list", CoreLib.listType), new Arg("value")},
                (vm, args, rResult, loc) -> {
                    args[0].cast(CoreLib.listType).add(args[1]);
                });
    }
}