package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Splat(int rTarget) implements Op {
    @Override
    public Code code() {
        return Code.Splat;
    }

    @Override
    public Object data() {
        return rTarget;
    }

    @Override
    public String dump(final VM vm) {
        return "Splat rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }
}