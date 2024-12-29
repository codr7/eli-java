package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record Next(int rIter, int rItem, Label bodyEnd) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rIter);
        write.add(rItem);
    }

    public static Op make(final int rIter, final int rItem, final Label bodyEnd, final Loc loc) {
        return new Op(OpCode.NEXT, new Next(rIter, rItem, bodyEnd), loc);
    }

    public String toString(final VM vm) {
        return "rIter: " + rIter + " (" + vm.registers.get(rIter).dump(vm) + ") " +
                "rItem: " + rItem + " (" + ((rItem == -1) ? "n/a" : vm.registers.get(rItem).dump(vm)) + ") " +
                "bodyEnd: " + bodyEnd;
    }
}
