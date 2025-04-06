package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.Value;
import codr7.eli.libs.CoreLib;

public record Inc(int rTarget, long delta, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Inc rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") delta: " + delta;
    }

    @Override
    public void eval(final VM vm) {
        final var v = vm.registers.get(rTarget).cast(CoreLib.Int);
        vm.registers.set(rTarget, new Value<>(CoreLib.Int, v + delta));
        vm.pc++;
    }
}
