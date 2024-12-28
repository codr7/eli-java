package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.util.Set;

public record Dec(int rTarget, long delta) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }

    public static Op make(final int rTarget, final long delta, final Loc loc) {
        return new Op(OpCode.DEC, new Dec(rTarget, delta), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "delta: " + delta;
    }
}
