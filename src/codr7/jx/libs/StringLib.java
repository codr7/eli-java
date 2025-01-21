package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import codr7.jx.Value;

public class StringLib extends Lib {
    public StringLib() {
        super("string");

        bindMethod("find", new Arg[]{new Arg("in", CoreLib.stringType), new Arg("it", CoreLib.stringType)}, CoreLib.intType,
                (vm, args, rResult, loc) -> {
                        final var in = args[0].cast(CoreLib.stringType);
                        final var it = args[0].cast(CoreLib.stringType);
                        final var i = in.indexOf(it);
                        vm.registers.set(rResult, (i == -1) ? CoreLib.NIL : new Value<>(CoreLib.intType, (long) i));
                });

        bindMethod("strip", new Arg[]{new Arg("in", CoreLib.stringType), new Arg("it", CoreLib.anyType)}, CoreLib.stringType,
                (vm, args, rResult, loc) -> {
                    final var set = args[1].cast(CoreLib.charType);
                    final var in = args[0].cast(CoreLib.stringType);
                    final var out = in.replaceAll("[" + set + "]", "");
                    vm.registers.set(rResult, new Value<>(CoreLib.stringType, out));
                });
    }
}