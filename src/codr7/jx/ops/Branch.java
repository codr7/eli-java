package codr7.jx.ops;

import codr7.jx.Op;
import codr7.jx.*;

import java.util.Set;

public record Branch(int rCondition, int elsePc) {
    public void io(final Set<Integer> read, final Set<Integer> write) {
        read.add(rCondition);
    }

    public static Op make(final int rCondition, final int elsePc, final Loc loc) {
        return new Op(OpCode.BRANCH, new Branch(rCondition, elsePc), loc);
    }

    public Op relocate(final int deltaPc, final Loc loc) {
        return make(rCondition, elsePc + deltaPc, loc);
    }

    public String toString(final VM vm) {
        return "rCondition: " + rCondition + " (" + vm.registers.get(rCondition).dump(vm) + ") " +
                "elsePc: " + elsePc;
    }
}
