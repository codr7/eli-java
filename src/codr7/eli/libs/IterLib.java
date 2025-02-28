package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.core.iters.StreamItems;
import codr7.eli.libs.core.traits.IterableTrait;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class IterLib extends Lib {
    public IterLib() {
        super("iter");

        bindMethod("combinations",
                new Arg[]{new Arg("in", CoreLib.iterableTrait)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    if (in.type() instanceof IterableTrait it) {
                        final var ol = new ArrayList<IValue>();
                        final var oi = it.iter(vm, in);

                        while (oi.next(vm, vm.rScratch, loc)) {
                            ol.add(vm.registers.get(vm.rScratch));
                        }

                        final var out = Utils.combine(ol.toArray(IValue[]::new));
                        final Stream<IValue> s = out.map(l -> new Value<>(CoreLib.listType, new ArrayList<>(l)));
                        vm.registers.set(rResult, new Value<>(CoreLib.iterType, new StreamItems(s)));
                    }
                });

        bindMethod("get", new Arg[]{new Arg("target", CoreLib.iterableTrait)},
                (vm, args, rResult, loc) -> {
                    final var t = args[0];

                    if (t.type() instanceof IterableTrait lt) {
                        vm.registers.set(rResult, new Value<>(CoreLib.iterType, lt.iter(vm, t)));
                    } else {
                        throw new EvalError("Expected iterable: " + t.dump(vm), loc);
                    }
                });

        bindMethod("unzip", new Arg[]{new Arg("in", CoreLib.iterableTrait)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];

                    if (in.type() instanceof IterableTrait it) {
                        final var lvs = new ArrayList<IValue>();
                        final var rvs = new ArrayList<IValue>();
                        final var i = it.iter(vm, in);

                        while (i.next(vm, vm.rScratch, loc)) {
                            final var p = vm.registers.get(vm.rScratch).cast(CoreLib.pairType);
                            lvs.add(p.left());
                            rvs.add(p.right());
                        }

                        vm.registers.set(rResult,
                                new Value<>(CoreLib.pairType,
                                        new Pair(new Value<>(CoreLib.listType, lvs),
                                                new Value<>(CoreLib.listType, rvs))));
                    } else {
                        throw new EvalError("Expected iterable: " + in.dump(vm), loc);
                    }
                });
    }
}