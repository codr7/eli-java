package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import codr7.jx.Value;

public class StringLib extends Lib {
    public StringLib() {
        super("string");

        bindMethod("find", new Arg[]{new Arg("in", CoreLib.stringType), new Arg("it", CoreLib.stringType)}, null,
                (vm, args, rResult, loc) -> {
                        final var in = args[0].cast(CoreLib.stringType);
                        final var it = args[0].cast(CoreLib.stringType);
                        final var i = in.indexOf(it);
                        vm.registers.set(rResult, (i == -1) ? CoreLib.NIL : new Value<>(CoreLib.intType, (long) i));
                });
    }
}