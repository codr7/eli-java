package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Check(int rValues, Loc loc) implements Op {
    public String dump(final VM vm) {
        return "Check rValues: " + rValues +
                " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues+1).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rValues);
        read.add(rValues+1);
    }
}