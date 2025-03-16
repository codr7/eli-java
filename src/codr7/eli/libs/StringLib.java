package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Util;
import codr7.eli.Value;
import codr7.eli.libs.core.StreamItems;

public final class StringLib extends Lib {
    public StringLib(final Lib parentLib) {
        super("string", parentLib);

        bindMethod("find",
                new Arg[]{new Arg("in", CoreLib.String), new Arg("it", CoreLib.String)},
                (vm, args, rResult, loc) -> {
                    final var in = args[0].cast(CoreLib.String);
                    final var it = args[0].cast(CoreLib.String);
                    final var i = in.indexOf(it);
                    vm.registers.set(rResult, (i == -1) ? CoreLib.NIL : new Value<>(CoreLib.Int, (long) i));
                });

        bindMethod("split",
                new Arg[]{new Arg("in", CoreLib.String), new Arg("sep", CoreLib.Int)},
                (vm, args, rResult, loc) -> {
                  final var ss = Util.split(args[0].cast(CoreLib.String), args[1].cast(CoreLib.Int).intValue());
                  final var it = new StreamItems(ss.map(s -> new Value<>(CoreLib.String, s)));
                  vm.registers.set(rResult, new Value<>(CoreLib.Iter, it));
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