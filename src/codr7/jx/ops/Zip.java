package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.util.Set;

public record Zip(int rLeft, int rRight, int rResult) {
    public void io(final Set<Integer> read, final Set<Integer> write) {
        read.add(rLeft);
        read.add(rRight);
        write.add(rResult);
    }

    public static Op make(final int rLeft, final int rRight, int rResult, final Loc loc) {
        return new Op(OpCode.ZIP, new Zip(rLeft, rRight, rResult), loc);
    }

    public String toString(final VM vm) {
        return "rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")" +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}