package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record CreateList(int rTarget) {
    public static Op make(final int rTarget, final Location location) {
        return new Op(OpCode.CREATE_LIST, new CreateList(rTarget), location);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }
}