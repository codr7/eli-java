package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Put(int rTarget, IValue value, Loc loc) implements Op {
    @Override
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
