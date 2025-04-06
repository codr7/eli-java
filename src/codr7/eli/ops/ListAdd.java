package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.Value;
import codr7.eli.libs.CoreLib;

public record ListAdd(int rTarget, int rItem, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "ListAdd rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var t = vm.registers.get(rTarget).cast(CoreLib.List);
        final var v = vm.registers.get(rItem);
        Value.expand(vm, v, t, loc);
        vm.pc++;
    }
}