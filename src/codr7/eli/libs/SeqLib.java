package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;

public final class SeqLib extends Lib {
    public SeqLib() {
        super("seq", null);

        bindMethod("head",
                new Arg[]{new Arg("seq", CoreLib.seqTrait)},
                (vm, args, rResult, loc) -> {
                    final var s = args[0];
                    final var st = s.type().cast(CoreLib.seqTrait, loc);
                    vm.registers.set(rResult, st.head(s));
        });

        bindMethod("tail",
                new Arg[]{new Arg("seq", CoreLib.seqTrait)},
                (vm, args, rResult, loc) -> {
                    final var s = args[0];
                    final var st = s.type().cast(CoreLib.seqTrait, loc);
                    vm.registers.set(rResult, st.tail(s));
                });
    }
}