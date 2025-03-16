package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;

public final class SeqLib extends Lib {
    public SeqLib() {
        super("seq", null);

        bindMethod("count", new Arg[]{new Arg("it")},
                (vm, args, rResult, loc) -> {
                    final var it = args[0];
                    final var n = (long) it.type().cast(CoreLib.Countable, loc).count(it);
                    vm.registers.set(rResult, new Value<>(CoreLib.Int, n));
                });

        bindMethod("head",
                new Arg[]{new Arg("seq", CoreLib.Sequential)},
                (vm, args, rResult, loc) -> {
                    final var s = args[0];
                    final var st = s.type().cast(CoreLib.Sequential, loc);
                    vm.registers.set(rResult, st.head(s));
                });

        bindMethod("tail",
                new Arg[]{new Arg("seq", CoreLib.Sequential)},
                (vm, args, rResult, loc) -> {
                    final var s = args[0];
                    final var st = s.type().cast(CoreLib.Sequential, loc);
                    vm.registers.set(rResult, st.tail(s));
                });
    }
}