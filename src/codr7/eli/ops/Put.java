package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Put(int rTarget, IValue value, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Put rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "value: " + value.dump(vm);
    }

    @Override
    public void eval(final VM vm) {
        vm.registers.set(rTarget, value.dup(vm));
        vm.pc++;
    }
}