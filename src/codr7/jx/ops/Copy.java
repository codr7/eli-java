package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Copy(int rFrom, int rTo) {
    public static Op make(final int rFrom, final int rTo, final Location location) {
        return new Op(OpCode.COPY, new Copy(rFrom, rTo), location);
    }

    public String toString(final VM vm) {
        return "rFrom: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ") " +
                "rTo: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ")";
    }
}
