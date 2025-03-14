package codr7.eli.libs;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.errors.EvalError;
import codr7.eli.forms.CallForm;
import codr7.eli.forms.IdForm;
import codr7.eli.forms.ListForm;
import codr7.eli.forms.PairForm;
import codr7.eli.libs.core.iters.IntRange;
import codr7.eli.libs.core.traits.*;
import codr7.eli.libs.core.types.*;
import codr7.eli.ops.Iter;
import codr7.eli.ops.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CoreLib extends Lib {
    public static final TraitType<Void> Any = new TraitType<>("Any");
    public static final NilType Nil = new NilType("Nil");
    public static final TraitType<CallableTrait> Callable = new TraitType<>("Callable");
    public static final TraitType<ComparableTrait> Comparable = new TraitType<>("Comparable");
    public static final TraitType<IterableTrait> Iterable = new TraitType<>("Iterable");
    public static final TraitType<CountableTrait> Countable = new TraitType<>("Countable");
    public static final TraitType<NumericTrait> Numeric = new TraitType<>("Numeric", Comparable);
    public static final FloatType Float = new FloatType("Float", Any, Numeric);
    public static final IntType Int = new IntType("Int", Any, Numeric);
    public static final TraitType<SequentialTrait> Sequential = new TraitType<>("Sequential");
    public static final BindingType Binding = new BindingType("Binding");
    public static final BitType Bit = new BitType("Bit");
    public static final CharType Char = new CharType("Char");
    public static final DispatchType Dispatch = new DispatchType("Dispatch", Callable);
    public static final ExprType Expr = new ExprType("Expr");
    public static final IterType Iter = new IterType("Iter", Iterable);
    public static final JMacroType JMacro = new JMacroType("JMacro");
    public static final LibType Lib = new LibType("Lib");
    public static final ListType List = new ListType("List", Callable, Comparable, Countable, Iterable, Sequential);
    public static final MapType Map = new MapType("Map", Callable, Comparable, Countable, Iterable, Sequential);
    public static final MetaType Meta = new MetaType("Meta");
    public static final MethodType Method = new MethodType("Method", Callable);
    public static final PairType Pair = new PairType("Pair", Comparable, Countable, Iterable, Sequential);
    public static final RangeType Range = new RangeType("Range");
    public static final SplatType Splat = new SplatType("Splat");
    public static final StringType String = new StringType("String", Callable, Comparable, Countable, Iterable, Sequential);
    public static final SymType Sym = new SymType("Sym");
    public static final TimeType Time = new TimeType("Time");
    public static final TimestampType Timestamp = new TimestampType("Timestamp");

    public static final IValue NIL = new Value<>(Nil, new Object());
    public static final IValue T = new Value<>(Bit, true);
    public static final IValue F = new Value<>(Bit, false);

    public CoreLib() {
        super("core", null);

        bind(Any);
        bind(Callable);
        bind(Iterable);
        bind(Countable);
        bind(Numeric);
        bind(Sequential);

        bind(Binding);
        bind(Bit);
        bind(Dispatch);
        bind(Float);
        bind(Expr);
        bind(Int);
        bind(Iter);
        bind(JMacro);
        bind(Lib);
        bind(List);
        bind(Map);
        bind(Meta);
        bind(Method);
        bind(Nil);
        bind(Pair);
        bind(Range);
        bind(Splat);
        bind(String);
        bind(Sym);
        bind(Time);
        bind(Timestamp);

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

                    for (final var af : argList) {
                        margs.add(af.toArg(vm, loc));
                    }

                    final var rArgs = vm.alloc(margs.size());

                    final var start = new Label();
                    final var end = new Label();
                    final var m = new Method(mid, margs.toArray(new Arg[0]), rArgs, rResult, start, end, loc);
                    final var skip = new Label();
                    vm.emit(new Goto(skip));
                    start.pc = vm.emitPc();

                    if (!lambda) {
                        vm.currentLib.bind(m);
                    }

                    vm.doLib(null, () -> {
                        vm.currentLib.bindMacro("return", new Arg[]{new Arg("args*")},
                                (_vm, returnArgs, _rResult, _loc) -> {
                                    _vm.doLib(null, () -> {
                                        Form.emit(_vm, returnArgs, _rResult);
                                    });

                                    _vm.emit(new Goto(end));
                                });

                        m.bindArgs(vm);
                        Form.emit(vm, args, rResult);
                    });

                    end.pc = vm.emitPc();
                    vm.emit(new Return());
                    skip.pc = vm.emitPc();

                    if (lambda) {
                        new Value<>(Method, m).emit(vm, rResult, loc);
                    }
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

                    vm.registers.set(rResult, new Value<>(Bit, result));
                });

        bindMethod("<", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var lhs = args[0];
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        final var rhs = args[i];

                        if (lhs.type() instanceof ComparableTrait ct) {
                            if (ct.compareValues(lhs, rhs) >= 0) {
                                result = false;
                                break;
                            }
                        } else {
                            throw new EvalError("Expected comparable: " + lhs.dump(vm), loc);
                        }

                        lhs = rhs;
                    }

                    vm.registers.set(rResult, new Value<>(Bit, result));
                });

        bindMethod(">", new Arg[]{new Arg("args*")},
                (vm, args, rResult, location) -> {
                    var lhs = args[0].cast(Int);
                    var result = true;

                    for (var i = 1; i < args.length; i++) {
                        final var rhs = args[i].cast(Int);

                        if (lhs <= rhs) {
                            result = false;
                            break;
                        }

                        lhs = rhs;
                    }

                    vm.registers.set(rResult, new Value<>(Bit, result));
                });

        bindMethod("+", new Arg[]{new Arg("args*")},
                (vm, args, rResult, loc) -> {
                    var result = args[0];

                    for (var i = 1; i < args.length; i++) {
                        final var a = args[i];

                        if (a.type() instanceof NumericTrait nt) {
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
                        if (v.type() instanceof NumericTrait nt) {
                            result = nt.sub(v);
                        } else {
                            throw new EvalError("Expected num: " + v.dump(vm), loc);
                        }
                    } else {
                        result = args[0];

                        for (var i = 1; i < args.length; i++) {
                            final var a = args[i];

                            if (a.type() instanceof NumericTrait nt) {
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

                        if (a.type() instanceof NumericTrait nt) {
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

                        if (a.type() instanceof NumericTrait nt) {
                            result = nt.div(result, a);
                        } else {
                            throw new EvalError("Expected num: " + a.dump(vm), loc);
                        }
                    }

                    vm.registers.set(rResult, result);
                });

        bindMacro("bench", new Arg[]{new Arg("reps", Int), new Arg("body*", Int)},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var reps = args.removeFirst().eval(vm).cast(Int);
                    final var bodyEnd = new Label();
                    vm.emit(new Bench(reps, bodyEnd, rResult, loc));
                    Form.emit(vm, args, rResult);
                    bodyEnd.pc = vm.emitPc();
                });

        bindMacro("check", new Arg[]{new Arg("expected"), new Arg("body*")},
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var rValues = vm.alloc(2);

                    vm.doLib(null, () -> {
                        args.removeFirst().emit(vm, rValues);
                        Form.emit(vm, args, rValues + 1);
                        vm.emit(new Check(rValues, location));
                    });
                });

        bindMethod("count", new Arg[]{new Arg("it")},
                (vm, args, rResult, loc) -> {
                    final var it = args[0];
                    final var n = (long) it.type().cast(Countable, loc).count(it);
                    vm.registers.set(rResult, new Value<>(Int, n));
                });

        bindMacro("dec", new Arg[]{new Arg("place"), new Arg("delta?")},
                (vm, args, rResult, loc) -> {
                    final var t = (IdForm) args[0];
                    final var v = vm.currentLib.find(t.id);

                    if (v == null || v.type() != Binding) {
                        throw new EmitError("Expected binding: " + t.dump(vm), t.loc());
                    }

                    final var rValue = v.cast(Binding).rValue();
                    var delta = -1L;

                    if (args.length > 1) {
                        final var df = args[1];
                        final var dv = df.value(vm);
                        if (dv == null) {
                            throw new EmitError("Expected delta: " + df.dump(vm), loc);
                        }
                        delta = -dv.cast(Int);
                    }

                    vm.emit(new Inc(rValue, delta, loc));
                    if (rResult != rValue) {
                        vm.emit(new Copy(rValue, rResult, loc));
                    }
                });

        bindMacro("do", new Arg[]{new Arg("body*")},
                (vm, args, rResult, location) ->
                        vm.doLib(null, () -> Form.emit(vm, args, rResult)));

        bindMacro("for",
                new Arg[]{new Arg("bindings", List), new Arg("body*")},
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
                                    _vm.emit(new Goto(skipElse));
                                    elseStart.pc = _vm.emitPc();
                                    bodyLib.drop("else");
                                    Form.emit(_vm, _body, _rResult);
                                    skipElse.pc = _vm.emitPc();
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

                        Form.emit(vm, args, rResult);
                        if (elseStart.pc == -1) {
                            elseStart.pc = vm.emitPc();
                        }
                    });
                });

        bindMacro("inc", new Arg[]{new Arg("place"), new Arg("delta?")},
                (vm, args, rResult, loc) -> {
                    final var t = (IdForm) args[0];
                    final var v = vm.currentLib.find(t.id);

                    if (v == null || v.type() != Binding) {
                        throw new EmitError("Expected binding: " + t.dump(vm), t.loc());
                    }

                    final var rValue = v.cast(Binding).rValue();
                    var delta = 1L;

                    if (args.length > 1) {
                        final var df = args[1];
                        final var dv = df.value(vm);
                        if (dv == null) {
                            throw new EmitError("Expected delta: " + df.dump(vm), loc);
                        }
                        delta = dv.cast(Int);
                    }

                    vm.emit(new Inc(rValue, delta, loc));
                    if (rResult != rValue) {
                        vm.emit(new Copy(rValue, rResult, loc));
                    }
                });

        bindMacro("include", new Arg[]{new Arg("files*")},
                (vm, args, rResult, loc) -> {
                    for (final var f : args) {
                        final var v = f.value(vm);

                        if (v == null) {
                            throw new EmitError("Expected filename: " + f.dump(vm), f.loc());
                        }

                        vm.load(Path.of(v.cast(String)), rResult);
                    }
                });

        bindMacro("import", new Arg[]{new Arg("ids*")},
                (vm, args, rResult, loc) -> {
                    for (final var f : args) {
                        switch (f) {
                            case IdForm idf: {
                                final var found = IdForm.find(vm.currentLib, idf.id, idf.loc());

                                if (found == null) {
                                    throw new EmitError("Not found: " + idf.dump(vm), idf.loc());
                                }

                                vm.currentLib.bind(found.id(), found.lib().find(found.id()));
                                break;
                            }
                            case PairForm pf: {
                                final var srcId = pf.left.cast(vm, IdForm.class).id;
                                final var dstId = pf.right.cast(vm, IdForm.class).id;
                                final var v = IdForm.get(vm.currentLib, srcId, pf.loc());
                                vm.currentLib.bind(dstId, v);
                                break;
                            }
                            default:
                                throw new EmitError("Expected id: " + f.dump(vm), f.loc());
                        }
                    }
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

                    vm.registers.set(rResult, new Value<>(Bit, result));
                });

        bindMacro("let", new Arg[]{new Arg("bindings", List), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var bsf = args.removeFirst();

                    if (bsf instanceof ListForm f) {
                        final var bs = f.items;

                        vm.doLib(null, () -> {
                            for (var i = 0; i < bs.length; i += 2) {
                                final var rValue = vm.alloc(1);
                                final var vf = bs[i + 1];
                                final var v = vf.value(vm);

                                if (v == null) {
                                    vf.emit(vm, rValue);
                                    bs[i].bindRegister(vm, rValue, null, loc);
                                } else {
                                    bs[i].bindValue(vm, v, loc);
                                }
                            }

                            Form.emit(vm, args, rResult);
                        });
                    } else {
                        throw new EmitError("Expected bindings: " + bsf.dump(vm), loc);
                    }
                });

        bindMacro("lib", new Arg[]{new Arg("id"), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = Form.toDeque(_args);
                    final var id = args.removeFirst().cast(vm, IdForm.class).id;
                    vm.doLibId(id, () -> Form.emit(vm, args, rResult));
                });

        bindMethod("load", new Arg[]{},
                (vm, args, rResult, loc) -> {
                    final var skip = new Label();
                    vm.emit(new Goto(skip));
                    final var startPc = vm.emitPc();

                    for (final var f : args) {
                        vm.load(Path.of(f.cast(String)), rResult);
                    }

                    skip.pc = vm.emitPc();
                    vm.eval(startPc);
                });

        bindMacro("load", new Arg[]{new Arg("files*")},
                (vm, args, rResult, loc) -> {
                    for (final var f : args) {
                        final var v = f.value(vm);

                        if (v == null) {
                            throw new EmitError("Expected filename: " + f.dump(vm), f.loc());
                        }

                        vm.load(Path.of(v.cast(String)), rResult);
                    }
                });

        bindMethod("now", new Arg[]{},
                (vm, args, rResult, loc) -> {
                    vm.registers.set(rResult, new Value<>(Timestamp, LocalDateTime.now()));
                });

        bindMethod("parse-float", new Arg[]{new Arg("in", String), new Arg("start?", Int)},
                (vm, args, rResult, loc) -> {
                    final var start = (args.length == 2) ? args[1].cast(Int).intValue() : 0;
                    final var in = args[0].cast(String).substring(start);
                    final var match = Pattern.compile("^\\s*(-?\\d*[.]\\d+).*").matcher(in);

                    if (match.find()) {
                        vm.registers.set(rResult, new Value<>(Pair, new Pair(
                                new Value<>(Float, new BigDecimal(match.group(1))),
                                new Value<>(Int, (long) match.end(1) + start))));
                    } else {
                        vm.registers.set(rResult, new Value<>(Pair, new Pair(CoreLib.NIL, CoreLib.NIL)));
                    }
                });

        bindMethod("parse-int", new Arg[]{new Arg("in", String), new Arg("start?", Int)},
                (vm, args, rResult, loc) -> {
                    final var start = (args.length == 2) ? args[1].cast(Int).intValue() : 0;
                    final var in = args[0].cast(String).substring(start);
                    final var match = Pattern.compile("^\\s*(-?\\d+).*").matcher(in);

                    if (match.find()) {
                        vm.registers.set(rResult, new Value<>(Pair, new Pair(
                                new Value<>(Int, Long.valueOf(match.group(1))),
                                new Value<>(Int, (long) match.end(1) + start))));
                    } else {
                        vm.registers.set(rResult, new Value<>(Pair, new Pair(CoreLib.NIL, CoreLib.NIL)));
                    }
                });

        bindMethod("pow", new Arg[]{new Arg("base", Int), new Arg("exp", Int)},
                (vm, args, rResult, loc) -> {
                    final var b = args[0].cast(Int);
                    final var e = args[1].cast(Int);
                    vm.registers.set(rResult, new Value<>(Int, (long) Math.pow(b, e)));
                });

        bindMethod("range",
                new Arg[]{new Arg("start", Int), new Arg("end", Int), new Arg("stride", Int)},
                (vm, args, rResult, loc) -> {
                    final var start = args[0].cast(Int);
                    final var end = args[1].cast(Int);
                    final var stride = args[2].cast(Int);
                    vm.registers.set(rResult, new Value<>(Iter, new IntRange(start, end, stride)));
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

                            if (b.type() == Binding) {
                                final var v = args[i + 1];
                                v.emit(vm, b.cast(Binding).rValue());
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
                    for (final var a : args) {
                        a.unquote(vm, rResult, loc);
                    }
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
                new Arg[]{new Arg("cond", Any), new Arg("body*")},
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var start = new Label(vm.emitPc());
                    final var end = new Label();
                    final var rCond = vm.alloc(1);
                    args.removeFirst().emit(vm, rCond);
                    vm.emit(new Branch(rCond, end, loc));

                    vm.doLib(null, () -> {
                        vm.currentLib.bindMacro("break", new Arg[]{new Arg("args*")},
                                (_vm, _breakArgs, _rResult, _loc) -> {
                                    _vm.doLib(null, () -> Form.emit(_vm, _breakArgs, _rResult));
                                    _vm.emit(new Goto(end));
                                });

                        vm.currentLib.bindMacro("next", new Arg[]{new Arg("args*")},
                                (_vm, _breakArgs, _rResult, _loc) -> {
                                    _vm.doLib(null, () -> Form.emit(_vm, _breakArgs, _rResult));
                                    _vm.emit(new Goto(start));
                                });

                        Form.emit(vm, args, rResult);
                    });


                    vm.emit(new Goto(start));
                    end.pc = vm.emitPc();
                });
    }
}
