package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record AddItem(int rTarget, int rItem) {
    public static Op make(final int rTarget, final int rItem, final Location location) {
        return new Op(OpCode.ADD_ITEM, new AddItem(rTarget, rItem), location);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }
}