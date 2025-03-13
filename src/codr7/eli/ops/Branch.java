package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Branch(int rCondition, Label elseStart, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Branch;
    }

    @Override
    public String dump(final VM vm) {
        return "Branch rCondition: " + rCondition + " (" + vm.registers.get(rCondition).dump(vm) + ") " +
                "elseStart: " + elseStart;
    }
}
