package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Right(int rPair, int rResult, Loc loc) implements Op {
    public Code code() {
        return Code.Right;
    }

    @Override public String dump(final VM vm) {
        return "Right rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    @Override public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rPair);
        write.add(rResult);
    }
}