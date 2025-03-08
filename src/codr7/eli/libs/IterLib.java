package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.libs.core.iters.StreamItems;
import codr7.eli.libs.core.traits.IterableTrait;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class IterLib extends Lib {
    public IterLib() {
        super("iter", null);

        bindMethod("combinations",
                new Arg[]{new Arg("in", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    if (in.type() instanceof IterableTrait it) {
                        final var ol = new ArrayList<IValue>();
                        final var oi = it.iter(vm, in);
                        final var rValue = vm.alloc(1);

                        while (oi.next(vm, rValue, loc)) {
                            ol.add(vm.registers.get(rValue));
                        }

                        final var out = Utils.combine(ol.toArray(IValue[]::new));
                        final Stream<IValue> s = out.map(l -> new Value<>(CoreLib.List, new ArrayList<>(l)));
                        vm.registers.set(rResult, new Value<>(CoreLib.Iter, new StreamItems(s)));
                    }
                });

        bindMethod("pop", new Arg[]{new Arg("in", CoreLib.Iter)},
                (vm, args, rResult, loc) -> {
                    if (!args[0].cast(CoreLib.Iter).next(vm, rResult, loc)) {
                        vm.registers.set(rResult, CoreLib.NIL);
                    }
                });

        bindMethod("reduce",
                new Arg[]{new Arg("callback", CoreLib.Callable),
                        new Arg("in", CoreLib.Iterable),
                        new Arg("seed", CoreLib.Any)},
                (vm, args, rResult, loc) -> {
                    final var c = args[0];
                    final var ct = c.type().cast(CoreLib.Callable, loc);
                    final var in = args[1];
                    final var it = in.type().cast(CoreLib.Iterable, loc);
                    final var rArgs = vm.alloc(2);
                    vm.registers.set(rArgs, args[2]);

                    for (final var i = it.iter(vm, in); i.next(vm, rArgs + 1, loc); ) {
                        ct.call(vm, c, rArgs, 2, rArgs, true, loc);
                    }

                    vm.registers.set(rResult, vm.registers.get(rArgs));
                });

        bindMethod("unzip", new Arg[]{new Arg("in", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    final var it = in.type().cast(CoreLib.Iterable, loc);
                    final var lvs = new ArrayList<IValue>();
                    final var rvs = new ArrayList<IValue>();
                    final var i = it.iter(vm, in);
                    final var rValue = vm.alloc(1);

                    while (i.next(vm, rValue, loc)) {
                        final var p = vm.registers.get(rValue).cast(CoreLib.Pair);
                        lvs.add(p.left());
                        rvs.add(p.right());
                    }

                    vm.registers.set(rResult,
                            new Value<>(CoreLib.Pair,
                                    new Pair(new Value<>(CoreLib.List, lvs),
                                            new Value<>(CoreLib.List, rvs))));
                });
    }

    @Override
    public void init(final VM vm, final Loc loc) {
        importFrom(vm.coreLib, "^", "+");

        vm.eval("""
                (^sum [in]
                  (reduce + in 0))
                """, loc);
    }
}