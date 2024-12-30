package codr7.jx.ops;

import codr7.jx.*;
import codr7.jx.libs.Core;

import java.util.Set;

public record CallValue(IValue target, int rArgs, int arity, int rResult) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        for (var i = 0; i < arity; i++) { read.add(rArgs + i); }
        write.add(rResult);

        if (target.type() == Core.methodType) {
            final var m = target.cast(Core.methodType);
            for (var i = 0; i < m.args().length; i++) { read.add(m.rArgs()+i); }
            write.add(m.rResult());
        }
    }

    public static Op make(final IValue target,
                          final int rArgs,
                          final int arity,
                          final int rResult,
                          final Loc loc) {
        return new Op(OpCode.CALL_VALUE, new CallValue(target, rArgs, arity, rResult), loc);
    }

    public String toString(final VM vm) {
        return "target: " + target.dump(vm) + " " +
                "rArgs: " + rArgs + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}
