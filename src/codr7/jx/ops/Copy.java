package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Copy(int rFrom, int rTo) {
    public static Op make(final int rFrom, final int rTo, final Loc loc) {
        return new Op(OpCode.COPY, new Copy(rFrom, rTo), loc);
    }

    public String toString(final VM vm) {
        return "rFrom: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ") " +
                "rTo: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ")";
    }
}
