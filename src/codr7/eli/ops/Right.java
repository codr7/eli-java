package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record Right(int rPair, int rResult, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Right rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        vm.registers.set(rResult, vm.registers.get(rPair).cast(CoreLib.Pair).right());
        vm.pc++;
    }
}