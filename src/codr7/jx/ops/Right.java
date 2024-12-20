package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Right(int rPair, int rResult) {
    public static Op make(final int rPair, final int rResult, final Location location) {
        return new Op(OpCode.RIGHT, new Right(rPair, rResult), location);
    }

    public String toString(final VM vm) {
        return "rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}