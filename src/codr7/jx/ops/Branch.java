package codr7.jx.ops;

import codr7.jx.Op;
import codr7.jx.*;

public record Branch(int rCondition, int endPc) {
    public static Op make(final int rCondition, final int endPc, final Loc loc) {
        return new Op(OpCode.BRANCH, new Branch(rCondition, endPc), loc);
    }

    public String toString(final VM vm) {
        return "rCondition: " + rCondition + " (" + vm.registers.get(rCondition) + ") " +
                "endPc: " + endPc;
    }
}
