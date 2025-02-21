package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record CreateList(int rTarget, Loc loc) implements Op {
    public Code code() {
        return Code.CreateList;
    }

    public String dump(final VM vm) {
        return "CreateList rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        write.add(rTarget);
    }
}