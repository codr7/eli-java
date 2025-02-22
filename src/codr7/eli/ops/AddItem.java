package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record AddItem(int rTarget, int rItem, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.AddItem;
    }

    @Override
    public String dump(final VM vm) {
        return "AddItem rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rItem);
        read.add(rTarget);
        write.add(rTarget);
    }
}