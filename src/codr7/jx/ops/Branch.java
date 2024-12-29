package codr7.jx.ops;

import codr7.jx.Op;
import codr7.jx.*;

import java.util.Set;

public record Branch(int rCondition, Label elseStart) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rCondition);
    }

    public static Op make(final int rCondition, final Label elseStart, final Loc loc) {
        return new Op(OpCode.BRANCH, new Branch(rCondition, elseStart), loc);
    }

    public String toString(final VM vm) {
        return "rCondition: " + rCondition + " (" + vm.registers.get(rCondition).dump(vm) + ") " +
                "elseStart: " + elseStart;
    }
}
