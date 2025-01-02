package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Dec(int rTarget, long delta, Loc loc) implements Op {
    public String dump(final VM vm) {
        return "DEC rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "delta: " + delta;
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }
}
