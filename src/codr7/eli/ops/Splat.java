package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Splat(int rTarget, Loc loc) implements Op {
    public Code code() {
        return Code.Splat;
    }

    public String dump(final VM vm) {
        return "Splat rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }
}