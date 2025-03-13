package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Inc(int rTarget, long delta, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Inc;
    }

    @Override
    public String dump(final VM vm) {
        return "Inc rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") delta: " + delta;
    }
}
