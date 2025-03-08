package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Utils;
import codr7.eli.Value;

public final class BitLib extends Lib {
    public BitLib() {
        super("bit", null);

        bindMethod("<<",
                new Arg[]{new Arg("value", CoreLib.intType), new Arg("n", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.intType);
                    final var n = args[0].cast(CoreLib.intType);
                    vm.registers.set(rResult, new Value<>(CoreLib.intType, v << n));
                });

        bindMethod(">>",
                new Arg[]{new Arg("value", CoreLib.intType), new Arg("n", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.intType);
                    final var n = args[0].cast(CoreLib.intType);
                    vm.registers.set(rResult, new Value<>(CoreLib.intType, v >>> n));
                });

        bindMethod("len", new Arg[]{new Arg("value", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var v = args[0].cast(CoreLib.intType).intValue();
                    vm.registers.set(rResult, new Value<>(CoreLib.intType, (long) Utils.log2(v)));
                });
    }
}