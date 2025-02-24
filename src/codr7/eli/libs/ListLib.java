package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.IValue;
import codr7.eli.Lib;
import codr7.eli.Value;

import java.util.ArrayList;
import java.util.Arrays;

public final class ListLib extends Lib {
    public ListLib() {
        super("list");

        bindMethod("alloc",
                new Arg[]{new Arg("n", CoreLib.intType)},
                (vm, args, rResult, loc) -> {
                    final var vs = new IValue[args[0].cast(CoreLib.intType).intValue()];
                    Arrays.fill(vs, CoreLib.NIL);
                    vm.registers.set(rResult, new Value<>(CoreLib.listType, new ArrayList<>(Arrays.asList(vs))));
                });
    }
}