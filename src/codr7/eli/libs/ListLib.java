package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.libs.core.iters.StreamItems;
import codr7.eli.libs.core.traits.IterTrait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public final class ListLib extends Lib {
    public ListLib() {
        super("list");

        bindMethod("alloc",
                new Arg[]{new Arg("n", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var vs = new IValue[args[0].cast(CoreLib.intType).intValue()];
                    Arrays.fill(vs, CoreLib.NIL);
                    vm.registers.set(rResult, new Value<>(CoreLib.listType, new ArrayList<>(Arrays.asList(vs))));
                });

        bindMethod("combine",
                new Arg[]{new Arg("in", CoreLib.iterableTrait)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    if (in.type() instanceof IterTrait it) {
                        final var ol = new ArrayList<IValue>();
                        final var oi = it.iter(vm, in);
                        final var rValue = vm.alloc(1);

                        while (oi.next(vm, rValue, loc)) {
                            ol.add(vm.registers.get(rValue));
                        }

                        final var out = Utils.combine(ol.toArray(IValue[]::new));
                        final Stream<IValue> s = out.map(l -> new Value<>(CoreLib.listType, new ArrayList<>(l)));
                        vm.registers.set(rResult, new Value<>(CoreLib.iterType, new StreamItems(s)));
                    }
                });
    }
}