package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record CallRegister(int rTarget, int rArguments, int arity, int rResult) {
    public static Op make(final int rTarget,
                          final int rArguments,
                          final int arity,
                          final int rResult,
                          final Loc loc) {
        return new Op(OpCode.CALL_REGISTER, new CallRegister(rTarget, rArguments, arity, rResult), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rArguments: " + rArguments + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}
