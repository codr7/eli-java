package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Unzip(int rPair, int rLeft, int rRight, Loc loc) implements Op {
    public Code code() {
        return Code.Unzip;
    }

    public String dump(final VM vm) {
        return "Unzip rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rPair);
        write.add(rLeft);
        write.add(rRight);
    }
}