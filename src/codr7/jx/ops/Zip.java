package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Zip(int rLeft, int rRight, int rResult) {
    public static Op make(final int rLeft, final int rRight, int rResult, final Location location) {
        return new Op(OpCode.ZIP, new Zip(rLeft, rRight, rResult), location);
    }

    public String toString(final VM vm) {
        return "rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")" +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}