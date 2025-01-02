package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record AddItem(int rTarget, int rItem, Loc loc) implements Op {
    public String dump(final VM vm) {
        return "ADD_ITEM rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rItem);
        read.add(rTarget);
        write.add(rTarget);
    }
}