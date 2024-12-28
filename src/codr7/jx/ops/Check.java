package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.util.Set;

public record Check(int rValues) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rValues);
        read.add(rValues+1);
    }

    public static Op make(final int rValues, final Loc loc) {
        return new Op(OpCode.CHECK, new Check(rValues), loc);
    }

    public String toString(final VM vm) {
        return "rValues: " + rValues +
            " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues+1).dump(vm) + ")";
    }
}