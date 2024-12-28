package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.util.Set;

public record AddItem(int rTarget, int rItem) {
    public void io(final Set<Integer> read, final Set<Integer> write) {
        read.add(rItem);
        write.add(rTarget);
    }

    public static Op make(final int rTarget, final int rItem, final Loc loc) {
        return new Op(OpCode.ADD_ITEM, new AddItem(rTarget, rItem), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }
}