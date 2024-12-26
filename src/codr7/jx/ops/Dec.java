package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Dec(int rTarget, long delta, int rResult) {
    public static Op make(final int rTarget, final long delta, final int rResult, final Loc loc) {
        return new Op(OpCode.DEC, new Dec(rTarget, delta, rResult), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "delta: " + delta + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";

    }
}
