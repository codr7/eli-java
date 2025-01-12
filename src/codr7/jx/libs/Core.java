package codr7.jx.libs;

import codr7.jx.*;
import codr7.jx.errors.EmitError;
import codr7.jx.forms.IdForm;
import codr7.jx.forms.ListForm;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.core.iters.IntRange;
import codr7.jx.libs.core.types.*;
import codr7.jx.ops.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class Core extends Lib {
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

    public static final IValue NIL = new Value<>(nilType, null);

    public static final IValue T = new Value<>(bitType, true);
    public static final IValue F = new Value<>(bitType, false);

    public Core() {
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
                            expected = new LiteralForm(Core.T, expected.loc());
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
                    var delta = 1L;

                    if (args.length > 1) {
                        final var d = args[1];
                        final var dv = d.value(vm);

                        if (dv == null || dv.type() != intType) {
                            throw new EmitError("Expected Int: " + d.dump(vm), t.loc());
                        }

                        delta = dv.cast(intType);
                    }

                    vm.emit(new Dec(rValue, delta, loc));
                    if (rResult != rValue) { vm.emit(new Copy(rValue, rResult, loc)); }
                });

        bindMacro("do", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    vm.doLib(null, () -> {
                        vm.emit(new ArrayDeque<>(Arrays.asList(args)), rResult);
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

                    if (bsf instanceof ListForm f) {
                        final var bs = f.items;

                        vm.doLib(null, () -> {
                            for (var i = 0; i < bs.length; i++) {
                                if (bs[i] instanceof IdForm idf) {
                                    i++;
                                    final var v = bs[i].value(vm);

                                    if (v == null) {
                                        final var rValue = vm.alloc(1);
                                        bs[i].emit(vm, rValue);
                                        vm.currentLib.bind(idf.id, new Value<>(bindingType, new Binding(null, rValue)));
                                    } else {
                                        vm.currentLib.bind(idf.id, v);
                                    }
                                }
                            }

                            vm.emit(args, rResult);
                        });
                    } else {
                        throw new EmitError("Expected list: " + bsf.dump(vm), loc);
                    }
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
                        vm.currentLib.bind(((IdForm) id).id, Core.bindingType, new Binding(value.type(), rValue));
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
    }
}
