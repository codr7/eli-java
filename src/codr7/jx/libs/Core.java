package codr7.jx.libs;

import codr7.jx.*;
import codr7.jx.errors.EmitError;
import codr7.jx.forms.IdForm;
import codr7.jx.forms.ListForm;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.core.types.*;
import codr7.jx.ops.Check;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class Core extends Lib {
    public static final BindingType bindingType = new BindingType("Binding");
    public static final BitType bitType = new BitType("Bit");
    public static final IntType intType = new IntType("Int");
    public static final JMacroType jMacroType = new JMacroType("JMacro");
    public static final JMethodType jMethodType = new JMethodType("JMethod");
    public static final LibType libType = new LibType("Lib");
    public static final ListType listType = new ListType("List");
    public static final MetaType metaType = new MetaType("Meta");
    public static final MethodType methodType = new MethodType("Method");
    public static final NilType nilType = new NilType("Nil");
    public static final PairType pairType = new PairType("Pair");
    public static final StringType stringType = new StringType("String");

    public static final IValue NIL = new Value<>(nilType, null);

    public static final IValue T = new Value<>(bitType, true);
    public static final IValue F = new Value<>(bitType, false);

    public Core() {
        super("core");

        bind(bindingType);
        bind(bitType);
        bind(intType);
        bind(jMacroType);
        bind(jMethodType);
        bind(libType);
        bind(listType);
        bind(metaType);
        bind(methodType);
        bind(nilType);
        bind(pairType);
        bind(stringType);

        bind("_", NIL);

        bind("T", T);
        bind("F", F);

        bindMacro("do", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    vm.doLib(() -> {
                        vm.emit(new ArrayDeque<>(Arrays.asList(args)), rResult);
                    });
                });

        bindMacro("check", new Arg[]{new Arg("expected"), new Arg("body*")}, null,
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var rValues = vm.alloc(2);

                    vm.doLib(() -> {
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
                        vm.emit(actual, rValues+1);
                        vm.emit(Check.make(rValues, location));
                    });
                });

        bindMacro("method", new Arg[]{new Arg("name"), new Arg("args"), new Arg("result"), new Arg("body*")}, null,
                (vm, _args, rResult, loc) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));
                    final var mid = ((IdForm)args.removeFirst()).id;
                    final var margs = new ArrayList<Arg>();
                    final var argList = ((ListForm)args.removeFirst()).items;
                    final IType resultType = args.removeFirst().getType(vm);

                    for (var i = 0; i < argList.length; i++) {
                        final var af = argList[i];

                        if (af instanceof IdForm aid) {
                            margs.add(new Arg(aid.id));
                        } else {
                            throw new EmitError("Invalid arg: " + af.dump(vm), af.loc());
                        }
                    }

                    final var rArgs = vm.alloc(margs.size());
                    final var skipPc = vm.emit(Nop.make(loc));
                    final var startPc = vm.emitPc();

                    vm.doLib(() -> {
                        for (var i = 0; i < margs.size(); i++) {
                            final var ma = margs.get(i);
                            vm.currentLib.bind(ma.id(), bindingType, new Binding(null,rArgs+i));
                        }

                        vm.emit(args, rResult);
                    });

                    final var endPc = vm.emitPc();
                    vm.ops.set(skipPc, Goto.make(endPc, loc));
                    final var m = new Method(mid, margs.toArray(new Arg[0]), rArgs, resultType, rResult, startPc, endPc);
                    vm.currentLib.bind(m);
            });

        bindMacro("var", new Arg[]{new Arg("name1"), new Arg("value1"), new Arg("rest*")}, null,
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<>(Arrays.asList(_args));

                    while (!args.isEmpty()) {
                        final var id = args.removeFirst();

                        if (!(id instanceof IdForm)) {
                            throw new EmitError("Expected id: " + id.dump(vm), location);
                        }

                        if (args.isEmpty()) { throw new EmitError("Missing value", location); }
                        final var value = args.removeFirst().eval(vm);
                        final var rValue = vm.alloc(1);
                        vm.registers.set(rValue, value);
                        vm.currentLib.bind(((IdForm)id).id, Core.bindingType, new Binding(value.type(), rValue));
                    }
                });

        bindMethod("say", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    final var buffer = new StringBuilder();
                    for (final var a: args) { buffer.append(a.toString(vm)); }
                    System.out.println(buffer.toString());
                });
    }
}
