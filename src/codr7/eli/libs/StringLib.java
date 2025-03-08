package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;

public final class StringLib extends Lib {
    public StringLib() {
        super("string", null);

        bindMethod("find",
                new Arg[]{new Arg("in", CoreLib.String), new Arg("it", CoreLib.String)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0].cast(CoreLib.String);
                    final var it = args[0].cast(CoreLib.String);
                    final var i = in.indexOf(it);
                    vm.registers.set(rResult, (i == -1) ? CoreLib.NIL : new Value<>(CoreLib.Int, (long) i));
                });

        bindMethod("strip",
                new Arg[]{new Arg("in", CoreLib.String), new Arg("it", CoreLib.Any)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0].cast(CoreLib.String);
                    final var set = args[1].cast(CoreLib.Char);
                    final var out = in.replaceAll("[" + set + "]", "");
                    vm.registers.set(rResult, new Value<>(CoreLib.String, out));
                });
    }
}