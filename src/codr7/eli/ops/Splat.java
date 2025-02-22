package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Splat(int rTarget) implements Op {
    @Override
    public Code code() {
        return Code.Splat;
    }

    @Override
    public String dump(final VM vm) {
        return "Splat rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
    }
}