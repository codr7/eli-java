package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Branch(int rCondition, Label elseStart, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Branch rCondition: " + rCondition + " (" + vm.registers.get(rCondition).dump(vm) + ") " +
                "elseStart: " + elseStart;
    }

    @Override
    public void eval(final VM vm) {
        vm.pc = vm.registers.get(rCondition).toBit(vm) ? vm.pc + 1 : elseStart.pc;
    }
}