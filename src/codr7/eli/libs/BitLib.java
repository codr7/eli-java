package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Utils;
import codr7.eli.Value;

public final class BitLib extends Lib {
    public BitLib() {
        super("bit", null);

        bindMethod("<<",
                new Arg[]{new Arg("value", CoreLib.Int), new Arg("n", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.Int);
                    final var n = args[0].cast(CoreLib.Int);
                    vm.registers.set(rResult, new Value<>(CoreLib.Int, v << n));
                });

        bindMethod(">>",
                new Arg[]{new Arg("value", CoreLib.Int), new Arg("n", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.Int);
                    final var n = args[0].cast(CoreLib.Int);
                    vm.registers.set(rResult, new Value<>(CoreLib.Int, v >>> n));
                });

        bindMethod("len", new Arg[]{new Arg("value", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.Int).intValue();
                    vm.registers.set(rResult, new Value<>(CoreLib.Int, (long) Utils.log2(v)));
                });
    }
}