package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Copy(int rFrom, int rTo, int count) {
    public static Op make(final int rFrom, final int rTo, final int count, final Loc loc) {
        return new Op(OpCode.COPY, new Copy(rFrom, rTo, count), loc);
    }

    public String toString(final VM vm) {
        return "rFrom: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ") " +
                "rTo: " + rTo + " (" + vm.registers.get(rTo).dump(vm) + ") " +
                "count: " + count;
    }
}
