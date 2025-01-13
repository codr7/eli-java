package codr7.jx.libs;

import codr7.jx.*;
import codr7.jx.errors.EmitError;
import codr7.jx.forms.IdForm;
import codr7.jx.forms.ListForm;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.core.iters.IntRange;
import codr7.jx.libs.core.types.*;
import codr7.jx.ops.*;

import java.util.*;
import java.util.regex.Pattern;

public class CoreLib extends Lib {
    public static final AnyType anyType = new AnyType("Any");
    public static final NilType nilType = new NilType("Nil");
    public static final MaybeType maybeType = new MaybeType("Maybe", anyType, nilType);

    public static final BindingType bindingType = new BindingType("Binding");
    public static final BitType bitType = new BitType("Bit");
    public static final FormType formType = new FormType("Form");
    public static final IntType intType = new IntType("Int");
    public static final IterType iterType = new IterType("Iter");
    public static final JMacroType jMacroType = new JMacroType("JMacro");
    public static final JMethodType jMethodType = new JMethodType("JMethod");
    public static final LibType libType = new LibType("Lib");
    public static final ListType listType = new ListType("List");
    public static final MetaType metaType = new MetaType("Meta");
    public static final MethodType methodType = new MethodType("Method");
    public static final PairType pairType = new PairType("Pair");
    public static final RangeType rangeType = new RangeType("Range");
    public static final StringType stringType = new StringType("String");
    public static final SymbolType symbolType = new SymbolType("Symbol");
    public static final TimeType timeType = new TimeType("Time");

    public static final IValue NIL = new Value<>(nilType, new Object());

    public static final IValue T = new Value<>(bitType, true);
    public static final IValue F = new Value<>(bitType, false);

    public CoreLib() {
        super("core");

        bind(anyType);
        bind(bindingType);
        bind(bitType);
        bind(intType);
        bind(iterType);
        bind(jMacroType);
        bind(jMethodType);
        bind(libType);
        bind(listType);
        bind(maybeType);
        bind(metaType);
        bind(methodType);
        bind(nilType);
        bind(pairType);
        bind(rangeType);
        bind(stringType);
        bind(symbolType);
        bind(timeType);

        bind("_", NIL);

        bind("T", T);
        bind("F", F);

        bindMacro("^", new Arg[]{new Arg("id"), new Arg("args"), new Arg("result"), new Arg("body*")}, null,
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var mid = ((IdForm) args.removeFirst()).id;
                    final var margs = new ArrayList<Arg>();
                    final var argList = ((ListForm) args.removeFirst()).items;
                    final var rf = args.removeFirst();
                    final IType resultType = rf.getType(vm, rf.loc());

                    for (var i = 0; i < argList.length; i++) {
                        final var af = argList[i];

                        if (af instanceof IdForm aid) {
                            margs.add(new Arg(aid.id));
                        } else {
                            throw new EmitError("Invalid arg: " + af.dump(vm), af.loc());
                        }
                    }

                    final var rArgs = vm.alloc(margs.size());
                    final var skipPc = vm.emit(new Nop());
                    final var m = new Method(
                            mid,
                            margs.toArray(new Arg[0]), rArgs,
                            resultType, rResult,
                            args.toArray(new IForm[0]),
                            vm.currentLib,
                            vm.label(vm.emitPc()), vm.label(-1));
                    m.emitBody(vm, rResult, loc);
                    m.end().pc = vm.emitPc();
                    vm.ops.set(skipPc, new Goto(m.end(), loc));
                    vm.currentLib.bind(m);
                    vm.registers.set(rResult, new Value<>(methodType, m));
                });

        bindMethod("=", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, location) -> {
                    final var lhs = args[0];
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        if (!lhs.eq(args[i])) {
                            result = false;
                            break;
                        }
                    }

                    vm.registers.set(rResult, new Value<>(bitType, result));
                });

        bindMethod(">", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, location) -> {
                    var lhs = args[0].cast(intType);
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        final var rhs = args[i].cast(intType);

                        if (lhs <= rhs) {
                            result = false;
                            break;
                        }

                        lhs = rhs;
                    }

                    vm.registers.set(rResult, new Value<>(bitType, result));
                });

        bindMethod("+", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, location) -> {
                    var result = 0L;
                    for (final var a: args) { result += a.cast(intType); }
                    vm.registers.set(rResult, new Value<>(intType, result));
                });

        bindMethod("-", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, location) -> {
                    var result = args[0].cast(intType);

                    if (args.length == 1) {
                        result = -result;
                    } else {
                        for (var i = 1; i < args.length; i++) {
                            result -= args[i].cast(intType);
                        }
                    }

                    vm.registers.set(rResult, new Value<>(intType, result));
                });

        bindMethod("*", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, loc) -> {
                    var result = 1L;

                    for (final var a : args) {
                        result *= a.cast(intType);
                    }

                    vm.registers.set(rResult, new Value<>(intType, result));
                });

        bindMacro("bench", new Arg[]{new Arg("reps", intType), new Arg("body*", intType)}, null,
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var reps = args.removeFirst().eval(vm).cast(intType);
                    final var benchPc = vm.emit(new Nop());
                    final var rIter = vm.alloc(1);
                    vm.emit(new Put(rIter, new Value<>(iterType, new IntRange(0, reps, 1)), loc));
                    final var bodyEnd = vm.label(-1);
                    final var iterPc = vm.emit(new Next(rIter, -1, bodyEnd, loc));
                    vm.emit(args, rResult);
                    vm.emit(new Goto(vm.label(iterPc), loc));
                    bodyEnd.pc = vm.emitPc();
                    vm.ops.set(benchPc, new Bench(vm.label(), rResult, loc));
                });

        bindMacro("check", new Arg[]{new Arg("expected"), new Arg("body*")}, null,
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var rValues = vm.alloc(2);

                    vm.doLib(null, () -> {
                        var expected = args.removeFirst();
                        Deque<IForm> actual;

                        if (args.isEmpty()) {
                            actual = new ArrayDeque<>();
                            actual.add(expected);
                            expected = new LiteralForm(CoreLib.T, expected.loc());
                        } else {
                            actual = args;
                        }

                        expected.emit(vm, rValues);
                        vm.emit(actual, rValues + 1);
                        vm.emit(new Check(rValues, location));
                    });
                });

        bindMacro("dec", new Arg[]{new Arg("place"), new Arg("delta?")}, null,
                (vm, args, rResult, loc) -> {
                    final var t = args[0];
                    final var v = t.value(vm);

                    if (v.type() != bindingType) {
                        throw new EmitError("Expected binding: " + t.dump(vm), t.loc());
                    }

                    final var rValue = v.cast(bindingType).rValue();
                    var rDelta = -1;

                    if (args.length > 1) {
                        rDelta = vm.alloc(1);
                        args[1].emit(vm, rDelta);
                    }

                    vm.emit(new Dec(rValue, rDelta, loc));
                    if (rResult != rValue) { vm.emit(new Copy(rValue, rResult, loc)); }
                });

        bindMacro("do", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    vm.doLib(null, () -> {
                        vm.emit(new ArrayDeque<>(Arrays.asList(args)), rResult);
                    });
                });

        bindMacro("for",
                new Arg[]{new Arg("bindings", listType), new Arg("body*")},
                null,
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
                                    final var v = sf.value(vm);
                                    final var rSeq = vm.alloc(1);
                                    final var rIt = idf.isNil() ? -1 : vm.alloc(1);
                                    brs.put(rSeq, rIt);

                                    if (v == null || v.type() == bindingType) {
                                        sf.emit(vm, rSeq);
                                        vm.emit(new CreateIter(rSeq, loc));
                                    } else if (v.type() instanceof SeqTrait st) {
                                        final var it = st.iter(vm, v, loc);
                                        vm.emit(new Put(rSeq, new Value<>(iterType, it), loc));
                                    } else {
                                        throw new EmitError("Expected seq: " + v.dump(vm), loc);
                                    }

                                    if (!idf.isNil()) {
                                        vm.currentLib.bind(idf.id, CoreLib.bindingType, new Binding(null, rIt));
                                    }
                                } else {
                                    throw new EmitError("Expected id: " + vf.dump(vm), loc);
                                }
                            }
                        } else {
                            throw new EmitError("Expected bindings: " + bsf.dump(vm), loc);
                        }

                        final var bodyStart = vm.label();
                        final var bodyEnd = vm.label(-1);

                        for (final var br: brs.entrySet()) {
                            vm.emit(new Next(br.getKey(), br.getValue(), bodyEnd, loc));
                        }

                        vm.emit(args, rResult);
                        vm.emit(new Goto(bodyStart, loc));
                        bodyEnd.pc = vm.emitPc();
                    });
                });

        bindMacro("if", new Arg[]{new Arg("cond"), new Arg("left"), new Arg("right?")}, null,
                (vm, args, rResult, loc) -> {
                    args[0].emit(vm, rResult);
                    final var branchPc = vm.emit(new Nop());
                    args[1].emit(vm, rResult);
                    final var skipElsePc = vm.emit(new Nop());
                    vm.ops.set(branchPc, new Branch(rResult, vm.label(), loc));
                    if (args.length > 2) { args[2].emit(vm, rResult); }
                    vm.ops.set(skipElsePc, new Goto(vm.label(), loc));
                });

        bindMacro("inc", new Arg[]{new Arg("place"), new Arg("delta?")}, null,
                (vm, args, rResult, loc) -> {
                    final var t = args[0];
                    final var v = t.value(vm);

                    if (v.type() != bindingType) {
                        throw new EmitError("Expected binding: " + t.dump(vm), t.loc());
                    }

                    final var rValue = v.cast(bindingType).rValue();
                    var rDelta = -1;

                    if (args.length > 1) {
                        rDelta = vm.alloc(1);
                        args[1].emit(vm, rDelta);
                    }

                    vm.emit(new Inc(rValue, rDelta, loc));
                    if (rResult != rValue) { vm.emit(new Copy(rValue, rResult, loc)); }
                });

        bindMethod("is", new Arg[]{new Arg("args*")}, null,
                (vm, args, rResult, loc) -> {
                    final var lhs = args[0];
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        if (!lhs.is(args[i])) {
                            result = false;
                            break;
                        }
                    }

                    vm.registers.set(rResult, new Value<>(bitType, result));
                });

        bindMacro("let", new Arg[]{new Arg("bindings", listType), new Arg("body*")}, null,
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var bsf = args.removeFirst();
                    final var rrs = new HashMap<Integer, Integer>();

                    if (bsf instanceof ListForm f) {
                        final var bs = f.items;
                        final var parentLib = vm.currentLib;

                        vm.doLib(null, () -> {
                            for (var i = 0; i < bs.length; i++) {
                                if (bs[i] instanceof IdForm idf) {
                                    i++;
                                    final var prev = parentLib.find(idf.id);

                                    if (prev != null && prev.type() == bindingType) {
                                        final var rValue = prev.cast(bindingType).rValue();
                                        final var rPrev = vm.alloc(1);
                                        vm.emit(new Copy(rValue, rPrev, loc));
                                        bs[i].emit(vm, rValue);
                                        rrs.put(rPrev, rValue);
                                    } else {
                                        final var rValue = vm.alloc(1);
                                        bs[i].emit(vm, rValue);
                                        vm.currentLib.bind(idf.id, new Value<>(bindingType, new Binding(null, rValue)));
                                    }
                                }
                            }

                            vm.emit(args, rResult);
                        });

                        for (final var rr: rrs.entrySet()) {
                            vm.emit(new Copy(rr.getKey(), rr.getValue(), loc));
                        }
                    } else {
                        throw new EmitError("Expected bindings: " + bsf.dump(vm), loc);
                    }
            });

        bindMethod("parse-int", new Arg[]{new Arg("in")}, null,
                (vm, args, rResult, loc) -> {
            final var start = (args.length == 2) ? args[1].cast(intType).intValue() : 0;
            final var in = args[0].cast(stringType).substring(start);
            final var match = Pattern.compile("^\\s*(\\d+).*").matcher(in);

            if (match.find()) {
                vm.registers.set(rResult, new Value<>(pairType, new Pair(
                        new Value<>(intType, Long.valueOf(match.group(1))),
                        new Value<>(intType, (long) match.end(1) + start))));
            } else {
                vm.registers.set(rResult, CoreLib.NIL);
            }
        });

        bindMethod("say", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    final var buffer = new StringBuilder();
                    for (final var a : args) {
                        buffer.append(a.toString(vm));
                    }
                    System.out.println(buffer);
                });

        bindMacro("var", new Arg[]{new Arg("name1"), new Arg("value1"), new Arg("rest*")}, null,
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));

                    while (!args.isEmpty()) {
                        final var id = args.removeFirst();

                        if (!(id instanceof IdForm)) {
                            throw new EmitError("Expected id: " + id.dump(vm), location);
                        }

                        if (args.isEmpty()) {
                            throw new EmitError("Missing value", location);
                        }
                        final var value = args.removeFirst().eval(vm);
                        final var rValue = vm.alloc(1);
                        vm.registers.set(rValue, value);
                        vm.currentLib.bind(((IdForm) id).id, CoreLib.bindingType, new Binding(value.type(), rValue));
                    }
                });
    }
}
