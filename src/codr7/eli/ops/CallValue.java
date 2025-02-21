package codr7.eli.ops;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

import java.util.Set;

public record CallValue(IValue target, int rArgs, int arity, int rResult, Loc loc) implements Op {
    public Code code() {
        return Code.CallValue;
    }

    public String dump(final VM vm) {
        return "CallValue target: " + target.dump(vm) + " " +
                "rArgs: " + rArgs + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        for (var i = 0; i < arity; i++) { read.add(rArgs + i); }
        write.add(rResult);

        if (target.type() == CoreLib.methodType) {
            final var m = target.cast(CoreLib.methodType);
            for (var i = 0; i < m.args().length; i++) { read.add(m.rArgs()+i); }
            write.add(m.rResult());
        }
    }
}
