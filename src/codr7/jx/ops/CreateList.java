package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.util.Set;

public record CreateList(int rTarget) {
    public void io(final Set<Integer> read, final Set<Integer> write) {
        write.add(rTarget);
    }

    public static Op make(final int rTarget, final Loc loc) {
        return new Op(OpCode.CREATE_LIST, new CreateList(rTarget), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }
}