package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Check(int rValues) {
    public static Op make(final int rValues, final Location location) {
        return new Op(OpCode.CHECK, new Check(rValues), location);
    }

    public String toString(final VM vm) {
        return "rValues: " + rValues +
            " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues+1).dump(vm) + ")";
    }
}