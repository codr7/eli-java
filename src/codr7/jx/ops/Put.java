package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record Put(int rTarget, IValue value, Loc loc) implements Op {
    public Code code() {
        return Code.Put;
    }

    @Override
    public String dump(final VM vm) {
        return "Put rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "value: " + value.dump(vm);
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        write.add(rTarget);
    }
}
