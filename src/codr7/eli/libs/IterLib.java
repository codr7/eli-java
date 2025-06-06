package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.forms.IdForm;
import codr7.eli.forms.ListForm;
import codr7.eli.libs.core.IterableTrait;
import codr7.eli.libs.core.StreamItems;
import codr7.eli.libs.iter.ConcatResult;
import codr7.eli.libs.iter.CrossResult;
import codr7.eli.libs.iter.MapResult;
import codr7.eli.libs.iter.WhereResult;
import codr7.eli.ops.Goto;
import codr7.eli.ops.Next;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public final class IterLib extends Lib {
    public IterLib(final Lib parentLib) {
        super("iter", parentLib);

        bindMethod("all",
                new Arg[]{new Arg("predicate", CoreLib.Callable),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var p = args[0];
                    final var pt = p.type().cast(CoreLib.Callable, loc);

                    final var in = new Iter[args.length-1];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];
                        in[i-1] = a.type().cast(CoreLib.Iterable, loc).iter(vm, a);
                    }

                    final var rArgs = vm.alloc(in.length);
                    var result = true;

                    for (;;) {
                        var rArg = rArgs;

                        for (final var it : in) {
                            if (!it.next(vm, rArg, loc)) {
                                break;
                            }

                            rArg++;
                        }

                        if (rArg < rArgs+in.length) {
                            break;
                        }

                        pt.call(vm, p, rArgs, in.length, rResult, true, loc);

                        if (!vm.registers.get(rResult).toBit(vm)) {
                            result = false;
                            break;
                        }
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Bit, result));
                });

        bindMethod("any",
                new Arg[]{new Arg("predicate", CoreLib.Callable),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var p = args[0];
                    final var pt = p.type().cast(CoreLib.Callable, loc);

                    final var in = new Iter[args.length-1];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];
                        in[i-1] = a.type().cast(CoreLib.Iterable, loc).iter(vm, a);
                    }

                    final var rArgs = vm.alloc(in.length);
                    var result = false;

                    for (;;) {
                        var rArg = rArgs;

                        for (final var it : in) {
                            if (!it.next(vm, rArg, loc)) {
                                break;
                            }

                            rArg++;
                        }

                        if (rArg < rArgs+in.length) {
                            break;
                        }

                        pt.call(vm, p, rArgs, in.length, rResult, true, loc);

                        if (vm.registers.get(rResult).toBit(vm)) {
                            result = true;
                            break;
                        }
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Bit, result));
                });

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

                        final var out = Util.combine(ol.toArray(IValue[]::new));
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
                    final var cb = args[0];
                    final var xs = args[1];
                    final var ys = args[2];

                    vm.registers.set(rResult,
                            new Value<>(CoreLib.Iter, new CrossResult(vm, xs, ys, cb, loc)));
                });

        bindMethod("concat",
                new Arg[]{new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var its = new Iter[args.length];

                    for (var i = 0; i < args.length; i++) {
                        final var in = args[i];
                        its[i] = in.type().cast(CoreLib.Iterable, loc).iter(vm, in);
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Iter, new ConcatResult(its)));
                });

        bindMethod("fold",
                new Arg[]{new Arg("callback", CoreLib.Callable),
                        new Arg("seed", CoreLib.Any),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var c = args[0];
                    final var ct = c.type().cast(CoreLib.Callable, loc);
                    final var in = new Iter[args.length-2];

                    for (var i = 2; i < args.length; i++) {
                        final var a = args[i];
                        in[i-2] = a.type().cast(CoreLib.Iterable, loc).iter(vm, a);
                    }

                    final var rArgs = vm.alloc(in.length+1);
                    vm.registers.set(rArgs, args[1]);
                    var done = false;

                    for (;;) {
                        for (var i = 0; i < in.length; i++) {
                            if (!in[i].next(vm, rArgs + i + 1, loc)) {
                                done = true;
                                break;
                            }
                        }

                        if (done) {
                            break;
                        }

                        ct.call(vm, c, rArgs, in.length+1, rArgs, true, loc);
                    }

                    vm.registers.set(rResult, vm.registers.get(rArgs));
                });

        bindMacro("for",
                new Arg[]{new Arg("bindings"), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));

                    vm.doLib(null, () -> {
                        final var brs = new HashMap<Integer, Integer>();
                        final var bsf = args.removeFirst();

                        if (bsf instanceof ListForm bslf) {
                            final var bs = new ArrayDeque<>(Arrays.asList(bslf.items));

                            while (!bs.isEmpty()) {
                                final var vf = bs.removeFirst();
                                final var sf = bs.removeFirst();

                                if (vf instanceof IdForm idf) {
                                    final var v = sf.rawValue(vm);
                                    final var rSeq = vm.alloc(1);
                                    final var rIt = idf.isNil() ? -1 : vm.alloc(1);
                                    brs.put(rSeq, rIt);
                                    sf.emit(vm, rSeq);
                                    vm.emit(new codr7.eli.ops.Iter(rSeq));

                                    if (!idf.isNil()) {
                                        vm.currentLib.bind(idf.id, CoreLib.Binding, new Binding(null, rIt));
                                    }
                                } else {
                                    throw new EmitError("Expected id: " + vf.dump(vm), loc);
                                }
                            }
                        } else {
                            throw new EmitError("Expected bindings: " + bsf.dump(vm), loc);
                        }

                        final var bodyStart = new Label(vm.emitPc());
                        final var bodyEnd = new Label();

                        for (final var br : brs.entrySet()) {
                            vm.emit(new Next(br.getKey(), br.getValue(), bodyEnd, loc));
                        }

                        vm.doLib(null, () -> {
                            vm.currentLib.bindMacro("break", new Arg[]{new Arg("args*")},
                                    (_vm, _breakArgs, _rResult, _loc) -> {
                                        _vm.doLib(null, () -> Form.emit(_vm, _breakArgs, _rResult));
                                        _vm.emit(new Goto(bodyEnd));
                                    });

                            vm.currentLib.bindMacro("next", new Arg[]{new Arg("args*")},
                                    (_vm, _breakArgs, _rResult, _loc) -> {
                                        _vm.doLib(null, () -> Form.emit(_vm, _breakArgs, _rResult));
                                        _vm.emit(new Goto(bodyStart));
                                    });

                            Form.emit(vm, args, rResult);
                        });

                        vm.emit(new Goto(bodyStart));
                        bodyEnd.pc = vm.emitPc();
                    });
                });

        bindMethod("get", new Arg[]{new Arg("in", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0];
                    final var it = in.type().cast(CoreLib.Iterable, loc).iter(vm, in);
                    vm.registers.set(rResult, new Value<>(CoreLib.Iter, it));
                });

        bindMethod("map",
                new Arg[]{new Arg("callback", CoreLib.Callable),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var cb = args[0];
                    final var its = new Iter[args.length - 1];

                    for (var i = 0; i < args.length - 1; i++) {
                        final var in = args[i + 1];
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

        bindMethod("where",
                new Arg[]{new Arg("pred", CoreLib.Callable),
                        new Arg("in*", CoreLib.Iterable)},
                (vm, args, rResult, loc) -> {
                    final var p = args[0];
                    final var its = new Iter[args.length - 1];

                    for (var i = 0; i < args.length - 1; i++) {
                        final var in = args[i + 1];
                        its[i] = in.type().cast(CoreLib.Iterable, loc).iter(vm, in);
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Iter, new WhereResult(vm, its, p, loc)));
                });
    }

    @Override
    public void init(final VM vm, final Loc loc) {
        vm.eval("""
                (^sum [in*]
                  (fold + 0 in*))
                  
                (^zip [in*]
                  (map core/zip in*))
                """, loc);
    }
}