package codr7.jx.libs;

import codr7.jx.*;
import codr7.jx.libs.core.types.*;
import codr7.jx.ops.Check;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Core extends Lib {
    public static final BindingType bindingType = new BindingType("Binding");
    public static final BitType bitType = new BitType("Bit");
    public static final IntType intType = new IntType("Int");
    public static final JMacroType jMacroType = new JMacroType("JMacro");
    public static final LibType libType = new LibType("Lib");
    public static final MetaType metaType = new MetaType("Meta");
    public static final NilType nilType = new NilType("Nil");
    public static final PairType pairType = new PairType("Pair");

    public static final IValue NIL = new Value<>(nilType, null);

    public Core() {
        super("core");

        bind(bindingType);
        bind(bitType);
        bind(intType);
        bind(jMacroType);
        bind(libType);
        bind(metaType);
        bind(nilType);
        bind(pairType);

        bind("_", NIL);

        bind("T", new Value<>(bitType, true));
        bind("F", new Value<>(bitType, false));

        bindMacro("do", new Arg[]{new Arg("body*")}, null,
                (vm, args, rResult, location) -> {
                    vm.doLib(() -> {
                        vm.emit(new ArrayDeque<IForm>(Arrays.asList(args)), rResult);
                    });
                });

        bindMacro("check", new Arg[]{new Arg("expected"), new Arg("body*")}, null,
                (vm, _args, rResult, location) -> {
                    final var args = new ArrayDeque<IForm>(Arrays.asList(_args));
                    final var rValues = vm.alloc(2);

                    vm.doLib(() -> {
                        final var expected = args.removeFirst();
                        expected.emit(vm, rValues);
                        final var actual = args.isEmpty() ? expected : args.removeFirst();
                        actual.emit(vm, rValues+1);
                        vm.emit(Check.make(rValues, location));
                    });
                });
    }
}
