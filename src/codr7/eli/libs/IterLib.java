package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.libs.core.iters.StreamItems;
import codr7.eli.libs.core.traits.IterTrait;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class IterLib extends Lib {
    public IterLib() {
        super("iter", null);

        bindMethod("combinations",
                new Arg[]{new Arg("in", CoreLib.iterTrait)},
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

        bindMethod("pop", new Arg[]{new Arg("in", CoreLib.iterType)},
                (vm, args, rResult, loc) -> {
                    if (!args[0].cast(CoreLib.iterType).next(vm, rResult, loc)) {
                        vm.registers.set(rResult, CoreLib.NIL);
                    }
                });

        bindMethod("reduce",
                new Arg[]{new Arg("callback", CoreLib.callTrait),
                        new Arg("in", CoreLib.iterTrait),
                        new Arg("seed", CoreLib.anyType)},
                (vm, args, rResult, loc) -> {
            final var c = args[0];
            final var ct = c.type().cast(CoreLib.callTrait, loc);
            final var in = args[1];
            final var it = in.type().cast(CoreLib.iterTrait, loc);
            final var rArgs = vm.alloc(2);
            vm.registers.set(rArgs, args[2]);

            for (final var i = it.iter(vm, in); i.next(vm, rArgs+1, loc);) {
                ct.call(vm, c, rArgs, 2, rArgs,true, loc);
            }

            vm.registers.set(rResult, vm.registers.get(rArgs));
        });

        bindMethod("unzip", new Arg[]{new Arg("in", CoreLib.iterTrait)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    final var it = in.type().cast(CoreLib.iterTrait, loc);
                    final var lvs = new ArrayList<IValue>();
                    final var rvs = new ArrayList<IValue>();
                    final var i = it.iter(vm, in);
                    final var rValue = vm.alloc(1);

                    while (i.next(vm, rValue, loc)) {
                        final var p = vm.registers.get(rValue).cast(CoreLib.pairType);
                        lvs.add(p.left());
                        rvs.add(p.right());
                    }

                    vm.registers.set(rResult,
                            new Value<>(CoreLib.pairType,
                                    new Pair(new Value<>(CoreLib.listType, lvs),
                                            new Value<>(CoreLib.listType, rvs))));
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