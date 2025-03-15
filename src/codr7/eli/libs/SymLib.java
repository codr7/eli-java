package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;

public final class SymLib extends Lib {
    public SymLib() {
        super("sym", null);

        bindMethod("make",
                new Arg[]{new Arg("in*")},
                (vm, args, rResult, loc) -> {
                    final var out = new StringBuilder();

                    for (final var a : args) {
                        out.append(a.toString(vm));
                    }

                    vm.registers.set(rResult, new Value<>(CoreLib.Sym, out.toString()));
                });
    }
}