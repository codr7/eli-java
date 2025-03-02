package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.errors.EvalError;
import codr7.eli.forms.*;
import codr7.eli.libs.core.iters.IntRange;
import codr7.eli.libs.core.traits.CmpTrait;
import codr7.eli.libs.core.traits.NumTrait;
import codr7.eli.libs.core.traits.SeqTrait;
import codr7.eli.libs.core.types.*;
import codr7.eli.ops.*;
import codr7.eli.ops.Iter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class CoreLib extends Lib {
    public static final TraitType anyType = new TraitType("Any");
    public static final TraitType callableTrait = new TraitType("Callable");
    public static final TraitType iterableTrait = new TraitType("Iterable");
    public static final TraitType seqTrait = new TraitType("Seq");
    public static final NilType nilType = new NilType("Nil");
    public static final MaybeType maybeType = new MaybeType("Maybe", anyType, nilType);
    public static final TraitType numType = new TraitType("Num", anyType);

    public static final BindingType bindingType = new BindingType("Binding");
    public static final BitType bitType = new BitType("Bit");
    public static final CharType charType = new CharType("Char");
    public static final ExprType exprType = new ExprType("Expr");
    public static final FloatType floatType = new FloatType("Float");
    public static final IntType intType = new IntType("Int");
    public static final IterType iterType = new IterType("Iter");
    public static final JMacroType jMacroType = new JMacroType("JMacro");
    public static final JMethodType jMethodType = new JMethodType("JMethod");
    public static final LibType libType = new LibType("Lib");
    public static final ListType listType = new ListType("List", seqTrait);
    public static final MapType mapType = new MapType("Map", seqTrait);
    public static final MetaType metaType = new MetaType("Meta");
    public static final MethodType methodType = new MethodType("Method");
    public static final PairType pairType = new PairType("Pair", seqTrait);
    public static final RangeType rangeType = new RangeType("Range");
    public static final StringType stringType = new StringType("String", seqTrait);
    public static final SplatType splatType = new SplatType("Splat");
    public static final SymType symType = new SymType("Sym");
    public static final TimeType timeType = new TimeType("Time");
    public static final TimestampType timestampType = new TimestampType("Timestamp");

    public static final IValue NIL = new Value<>(nilType, new Object());
    public static final IValue T = new Value<>(bitType, true);
    public static final IValue F = new Value<>(bitType, false);

    public CoreLib() {
        super("core");

        bind(anyType);
        bind(bindingType);
        bind(bitType);
        bind(floatType);
        bind(exprType);
        bind(intType);
        bind(iterType);
        bind(jMacroType);
        bind(jMethodType);
        bind(libType);
        bind(listType);
        bind(maybeType);
        bind(mapType);
        bind(metaType);
        bind(methodType);
        bind(nilType);
        bind(numType);
        bind(pairType);
        bind(rangeType);
        bind(splatType);
        bind(stringType);
        bind(symType);
        bind(timeType);
        bind(timestampType);

        bind("_", NIL);
        bind("T", T);
        bind("F", F);

        bindMacro("^",
                new Arg[]{new Arg("id"), new Arg("args"), new Arg("result"), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    var f = args.removeFirst();
                    String mid = loc.toString();
                    var lambda = true;

                    if (f instanceof IdForm idf) {
                        mid = idf.id;
                        f = args.removeFirst();
                        lambda = false;
                    }

                    IForm[] argList;

                    if (f instanceof ListForm lf) {
                        argList = lf.items;
                    } else {
                        throw new EmitError("Missing arg list", f.loc());
                    }

                    final var margs = new ArrayList<Arg>();
                    for (IForm iForm : argList) { margs.add(new Arg(iForm.argId(vm, loc))); }
                    final var rArgs = vm.alloc(margs.size());

                    final var m = new Method(
                            mid,
                            margs.toArray(new Arg[0]), rArgs,
                            rResult,
                            args.toArray(new IForm[0]));

                    if (!lambda) { vm.currentLib.bind(m); }
                    vm.registers.set(rResult, new Value<>(methodType, m));
                });

        bindMethod("=", new Arg[]{new Arg("args*")},
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

        bindMethod("<", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var lhs = args[0];
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        final var rhs = args[i];

                        if (lhs.type() instanceof CmpTrait ct) {
                            if (ct.cmp(lhs, rhs) >= 0) {
                                result = false;
                                break;
                            }
                        } else {
                            throw new EvalError("Expected comparable: " + lhs.dump(vm), loc);
                        }

                        lhs = rhs;
                    }

                    vm.registers.set(rResult, new Value<>(bitType, result));
                });

        bindMethod(">", new Arg[]{new Arg("args*")},
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

        bindMethod("+", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var result = args[0];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];

                        if (a.type() instanceof NumTrait nt) {
                            result = nt.add(result, a);
                        } else {
                            throw new EvalError("Expected num: " + a.dump(vm), loc);
                        }
                    }

                    vm.registers.set(rResult, result);
                });

        bindMethod("-", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    IValue result = null;

                    if (args.length == 1) {
                        final var v = args[0];
                        if (v.type() instanceof NumTrait nt) { result = nt.sub(v); }
                        else { throw new EvalError("Expected num: " + v.dump(vm), loc); }
                    } else {
                        result = args[0];

                        for (var i = 1; i < args.length; i++) {
                            final var a = args[i];

                            if (a.type() instanceof NumTrait nt) {
                                result = nt.sub(result, a);
                            } else {
                                throw new EvalError("Expected num: " + a.dump(vm), loc);
                            }
                        }
                    }

                    vm.registers.set(rResult, result);
                });

        bindMethod("*", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var result = args[0];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];

                        if (a.type() instanceof NumTrait nt) {
                            result = nt.mul(result, a);
                        } else {
                            throw new EvalError("Expected num: " + a.dump(vm), loc);
                        }
                    }

                    vm.registers.set(rResult, result);
                });

        bindMethod("/", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var result = args[0];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];

                        if (a.type() instanceof NumTrait nt) {
                            result = nt.div(result, a);
                        } else {
                            throw new EvalError("Expected num: " + a.dump(vm), loc);
                        }
                    }

                    vm.registers.set(rResult, result);
                });

        bindMacro("bench", new Arg[]{new Arg("reps", intType), new Arg("body*", intType)},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var reps = args.removeFirst().eval(vm).cast(intType);
                    final var bodyEnd = new Label();
                    vm.emit(new Bench(reps, bodyEnd, rResult, loc));
                    vm.emit(args, rResult);
                    bodyEnd.pc = vm.emitPc();
                });

        bindMacro("check", new Arg[]{new Arg("expected"), new Arg("body*")},
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var rValues = vm.alloc(2);

                    vm.doLib(null, () -> {
                        args.removeFirst().emit(vm, rValues);
                        vm.emit(args, rValues + 1);
                        vm.emit(new Check(rValues, location));
                    });
                });

        bindMacro("dec", new Arg[]{new Arg("place"), new Arg("delta?")},
                (vm, args, rResult, loc) -> {
                    final var t = (IdForm)args[0];
                    final var v = vm.currentLib.find(t.id);

                    if (v == null || v.type() != bindingType) {
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

        bindMacro("do", new Arg[]{new Arg("body*")},
                (vm, args, rResult, location) -> {
                    vm.doLib(null, () -> {
                        vm.emit(new ArrayDeque<>(Arrays.asList(args)), rResult);
                    });
                });

        bindMacro("for",
                new Arg[]{new Arg("bindings", listType), new Arg("body*")},
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
                                    vm.emit(new Iter(rSeq));

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

                        final var bodyStart = new Label(vm.emitPc());
                        final var bodyEnd = new Label();

                        for (final var br: brs.entrySet()) {
                            vm.emit(new Next(br.getKey(), br.getValue(), bodyEnd, loc));
                        }

                        vm.doLib(null, () -> {
                            vm.currentLib.bindMacro("break", new Arg[]{new Arg("args*")},
                                    (_vm, _breakArgs, _rResult, _loc) -> {
                                        _vm.doLib(null, () -> {
                                            _vm.emit( new ArrayDeque<>(Arrays.asList(_breakArgs)), _rResult);
                                        });

                                        _vm.emit(new Goto(bodyEnd));
                                    });

                            vm.emit(args, rResult);
                        });

                        vm.emit(new Goto(bodyStart));
                        bodyEnd.pc = vm.emitPc();
                    });
                });

        bindMacro("if", new Arg[]{new Arg("cond"), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var rCond = vm.alloc(1);
                    args.removeFirst().emit(vm, rCond);
                    final var elseStart = new Label();
                    vm.emit(new Branch(rCond, elseStart, loc));

                    vm.doLib(null, () -> {
                        final var bodyLib = vm.currentLib;

                        vm.currentLib.bindMacro("else", new Arg[]{new Arg("body*")},
                            (_vm, _body, _rResult, _loc) -> {
                                final var skipElse = new Label();
                                vm.emit(new Goto(skipElse));
                                elseStart.pc = vm.emitPc();
                                bodyLib.drop("else");
                                vm.emit(_body, _rResult);
                                skipElse.pc = vm.emitPc();
                            });

                        vm.currentLib.bindMacro("else-if",
                                new Arg[]{new Arg("cond"), new Arg("body*")},
                                (_vm, _body, _rResult, _loc) -> {
                                    final var fs = new ArrayDeque<IForm>();
                                    fs.add(new IdForm("else", _loc));
                                    final var ifs = new ArrayDeque<IForm>();
                                    ifs.add(new IdForm("if", _loc));
                                    ifs.addAll(Arrays.asList(_body));
                                    fs.add(new CallForm(ifs.toArray(new IForm[0]), _loc));
                                    new CallForm(fs.toArray(new IForm[0]), _loc).emit(vm, _rResult);
                                });

                        vm.emit(args, rResult);
                        if (elseStart.pc == -1) { elseStart.pc = vm.emitPc(); }
                    });
                });

        bindMacro("inc", new Arg[]{new Arg("place"), new Arg("delta?")},
                (vm, args, rResult, loc) -> {
                    final var t = (IdForm)args[0];
                    final var v = vm.currentLib.find(t.id);

                    if (v == null || v.type() != bindingType) {
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

        bindMethod("is", new Arg[]{new Arg("args*")},
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

        bindMethod("len", new Arg[]{new Arg("it")},
                (vm, args, rResult, loc) -> {
            final var it = args[0];

            if (it.type() instanceof SeqTrait lt) {
                vm.registers.set(rResult, new Value<>(intType, (long) lt.len(it)));
            } else {
                throw new EvalError("Expected seq: " + it.dump(vm), loc);
            }
        });

        bindMacro("let", new Arg[]{new Arg("bindings", listType), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var bsf = args.removeFirst();

                    if (bsf instanceof ListForm f) {
                        final var bs = f.items;

                        vm.doLib(null, () -> {
                            for (var i = 0; i < bs.length; i += 2) {
                                final var rValue = vm.alloc(1);
                                final var vf = bs[i+1];
                                final var v = vf.value(vm);

                                if (v == null) {
                                    vf.emit(vm, rValue);
                                    bs[i].bindRegister(vm, rValue, loc);
                                } else {
                                    bs[i].bindValue(vm, v, loc);
                                }
                            }

                            vm.emit(args, rResult);
                        });
                    } else {
                        throw new EmitError("Expected bindings: " + bsf.dump(vm), loc);
                    }
            });

        bindMethod("next", new Arg[]{new Arg("in", iterType)},
                (vm, args, rResult, loc) -> {
                    if (!args[0].cast(iterType).next(vm, rResult, loc)) {
                        vm.registers.set(rResult, CoreLib.NIL);
                    }
                });

        bindMethod("now", new Arg[]{},
                (vm, args, rResult, loc) -> {
                    vm.registers.set(rResult, new Value<>(timestampType, LocalDateTime.now()));
                });

        bindMethod("parse-float", new Arg[]{new Arg("in")},
                (vm, args, rResult, loc) -> {
                    final var start = (args.length == 2) ? args[1].cast(intType).intValue() : 0;
                    final var in = args[0].cast(stringType).substring(start);
                    final var match = Pattern.compile("^\\s*(-?\\d*[.]\\d+).*").matcher(in);

                    if (match.find()) {
                        vm.registers.set(rResult, new Value<>(pairType, new Pair(
                                new Value<>(floatType, new BigDecimal(match.group(1))),
                                new Value<>(intType, (long) match.end(1) + start))));
                    } else {
                        vm.registers.set(rResult, new Value<>(pairType, new Pair(CoreLib.NIL, CoreLib.NIL)));
                    }
                });

        bindMethod("parse-int", new Arg[]{new Arg("in")},
                (vm, args, rResult, loc) -> {
            final var start = (args.length == 2) ? args[1].cast(intType).intValue() : 0;
            final var in = args[0].cast(stringType).substring(start);
            final var match = Pattern.compile("^\\s*(-?\\d+).*").matcher(in);

            if (match.find()) {
                vm.registers.set(rResult, new Value<>(pairType, new Pair(
                        new Value<>(intType, Long.valueOf(match.group(1))),
                        new Value<>(intType, (long) match.end(1) + start))));
            } else {
                vm.registers.set(rResult, new Value<>(pairType, new Pair(CoreLib.NIL, CoreLib.NIL)));
            }
        });

        bindMethod("pow", new Arg[]{new Arg("base", intType), new Arg("exp", intType)},
                (vm, args, rResult, loc) -> {
                    final var b = args[0].cast(intType);
                    final var e = args[1].cast(intType);
                    vm.registers.set(rResult, new Value<>(intType, (long)Math.pow(b, e)));
                });

        bindMethod("range",
                new Arg[]{new Arg("start", intType), new Arg("end", intType), new Arg("stride", intType)},
                (vm, args, rResult, loc) -> {
                    final var start = args[0].cast(intType);
                    final var end = args[1].cast(intType);
                    final var stride = args[2].cast(intType);
                    vm.registers.set(rResult, new Value<>(iterType, new IntRange(start, end, stride)));
                });

        bindMethod("say", new Arg[]{new Arg("body*")},
                (vm, args, rResult, location) -> {
                    final var buffer = new StringBuilder();

                    for (final var a : args) {
                        buffer.append(a.toString(vm));
                    }

                    System.out.println(buffer);
                });

        bindMacro("set", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    for (var i = 0; i < args.length; i += 2) {
                        final var id = args[i];
                        if (id instanceof IdForm f) {
                            final var b = f.rawValue(vm);

                            if (b.type() == bindingType) {
                                final var v = args[i + 1];
                                v.emit(vm, b.cast(bindingType).rValue());
                            } else {
                                throw new EmitError("Expected binding", f.loc());
                            }
                        } else {
                            throw new EmitError("Expected id", id.loc());
                        }
                    }
                });

        bindMacro("unquote", new Arg[]{new Arg("forms*")},
                (vm, args, rResult, loc) -> {
                    for (final var a: args) { a.unquote(vm, rResult, loc); }
                });

        bindMacro("var", new Arg[]{new Arg("name1"), new Arg("value1"), new Arg("rest*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));

                    while (!args.isEmpty()) {
                        final var id = args.removeFirst();

                        if (args.isEmpty()) {
                            throw new EmitError("Missing value", loc);
                        }

                        final var value = args.removeFirst().eval(vm);
                        id.bindValue(vm, value, loc);
                    }
                });

        bindMacro("while",
                new Arg[]{new Arg("cond", anyType), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var start = new Label(vm.emitPc());
                    final var end = new Label(vm.emitPc());
                    final var rCond = vm.alloc(1);
                    args.removeFirst().emit(vm, rCond);
                    vm.emit(new Branch(rCond, end, loc));

                    vm.doLib(null, () -> {
                        vm.currentLib.bindMacro("break", new Arg[]{new Arg("args*")},
                                (_vm, _breakArgs, _rResult, _loc) -> {
                                    _vm.doLib(null, () -> {
                                        _vm.emit( new ArrayDeque<>(Arrays.asList(_breakArgs)), _rResult);
                                    });

                                    _vm.emit(new Goto(end));
                                });

                        vm.emit(args, rResult);
                    });

                    vm.emit(new Goto(start));
                    end.pc = vm.emitPc();
                });
    }
}
