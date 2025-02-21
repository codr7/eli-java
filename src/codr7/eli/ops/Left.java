package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Left(int rPair, int rResult, Loc loc) implements Op {
    public Code code() {
        return Code.Left;
    }

    public String dump(final VM vm) {
        return "Left rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rPair);
        write.add(rResult);
    }
}