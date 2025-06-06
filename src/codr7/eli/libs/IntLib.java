package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;

public final class IntLib extends Lib {
    public IntLib(final Lib parentLib) {
        super("int", parentLib);

        bindMethod("rebase",
                new Arg[]{new Arg("value", CoreLib.Int), new Arg("base", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                    var in = args[0].cast(CoreLib.Int);
                    final var b = args[1].cast(CoreLib.Int);
                    var out = 0L;

                    while (in != 0) {
                        final var d = in / b;
                        final var r = in - d * b;
                        out = out * 10 + r;
                        in = d;
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Int, out));
                });
    }
}