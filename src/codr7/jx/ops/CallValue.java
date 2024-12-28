package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record CallValue(IValue target, int rArguments, int arity, int rResult) {
    public void io(final Set<Integer> read, final Set<Integer> write) {
        read.add(rArguments);
        write.add(rResult);
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
                "rArguments: " + rArguments + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}
