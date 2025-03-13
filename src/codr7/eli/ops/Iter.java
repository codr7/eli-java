package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Iter(int rTarget) implements Op {
    @Override
    public Code code() {
        return Code.Iter;
    }

    @Override
    public Object data() {
        return rTarget;
    }

    @Override
    public String dump(final VM vm) {
        return "Iter rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ")";
    }
}