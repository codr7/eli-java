package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Zip(int rLeft, int rRight, int rResult, Loc loc) implements Op {
    public Code code() {
        return Code.Zip;
    }

    public String dump(final VM vm) {
        return "Zip rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")" +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rLeft);
        read.add(rRight);
        write.add(rResult);
    }
}