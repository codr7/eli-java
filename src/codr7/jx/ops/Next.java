package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Next(int rIter, int rItem, int endPc) {
    public static Op make(final int rIter, final int rItem, final int endPc, final Loc loc) {
        return new Op(OpCode.NEXT, new Next(rIter, rItem, endPc), loc);
    }

    public String toString(final VM vm) {
        return "rIter: " + rIter + " (" + vm.registers.get(rIter).dump(vm) + ") " +
                "rItem: " + rItem + " (" + ((rItem == -1) ? "n/a" : vm.registers.get(rItem).dump(vm)) + ") " +
                "endPc: " + endPc;
    }
}
