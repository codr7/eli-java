package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.libs.core.StreamItems;
import codr7.eli.libs.core.IterableTrait;
import codr7.eli.libs.iter.MapResult;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class IterLib extends Lib {
    public IterLib() {
        super("iter", null);

        bindMethod("comb",
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
                        final Stream<IValue> s = out.map(l ->
                                new Value<>(CoreLib.List, new ArrayList<>(l)));
                        vm.registers.set(rResult, new Value<>(CoreLib.Iter, new StreamItems(s)));
                    }
                });

        bindMethod("cross",
                new Arg[]{new Arg("callback", CoreLib.Callable),
                        new Arg("xs", CoreLib.Iterable),
                        new Arg("ys", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var out = new ArrayList<IValue>();

                    final var cb = args[0];
                    final var ct = cb.type().cast(CoreLib.Callable, loc);
                    final var xs = args[1];
                    final var xi = xs.type().cast(CoreLib.Iterable, loc).iter(vm, xs);
                    final var ys = args[2];
                    final var rArgs = vm.alloc(2);

                    while (xi.next(vm, rArgs, loc)) {
                        final var yi = ys.type().cast(CoreLib.Iterable, loc).iter(vm, ys);

                        while (yi.next(vm, rArgs+1, loc)) {
                            ct.call(vm, cb, rArgs, 2, rResult, true, loc);
                            out.add(vm.registers.get(rResult));
                        }
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.List, out));
                });

        bindMethod("fold",
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

        bindMethod("map",
                new Arg[]{new Arg("callback", CoreLib.Callable),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var cb = args[0];
                    final var its = new Iter[args.length-1];

                    for (var i = 0; i < args.length-1; i++) {
                        final var in = args[i+1];
                        its[i] = in.type().cast(CoreLib.Iterable, loc).iter(vm, in);
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Iter, new MapResult(vm, its, cb, loc)));
                });

        bindMethod("pop", new Arg[]{new Arg("in", CoreLib.Iter)},
                (vm, args, rResult, loc) -> {
                    if (!args[0].cast(CoreLib.Iter).next(vm, rResult, loc)) {
                        vm.registers.set(rResult, CoreLib.NIL);
                    }
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
        importFrom(vm.coreLib, new String[]{"^", "+"}, loc);

        vm.eval("""
                (^sum [in]
                  (fold + in 0))
                """, loc);
    }
}