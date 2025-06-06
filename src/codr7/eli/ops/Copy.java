package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Copy(int rFrom, int rTo, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Copy rFrom: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ") " +
                "rTo: " + rTo + " (" + vm.registers.get(rTo).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        vm.registers.set(rTo, vm.registers.get(rFrom));
        vm.pc++;
    }
}
