package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.Value;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.IterableTrait;

public record Iter(int rTarget) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Iter rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var t = vm.registers.get(rTarget);
        final var it = ((IterableTrait) t.type()).iter(vm, t);
        vm.registers.set(rTarget, new Value<>(CoreLib.Iter, it));
        vm.pc++;
    }
}