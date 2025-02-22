package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record CreateIter(int rTarget, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.CreateIter;
    }

    @Override
    public String dump(final VM vm) {
        return "CreateIter rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }
}