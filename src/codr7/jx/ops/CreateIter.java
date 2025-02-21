package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record CreateIter(int rTarget, Loc loc) implements Op {
    public Code code() {
        return Code.CreateIter;
    }

    public String dump(final VM vm) {
        return "CreateIter rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }
}