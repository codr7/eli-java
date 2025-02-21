package codr7.jx.ops;

import codr7.jx.Op;
import codr7.jx.*;

import java.util.Set;

public record Branch(int rCondition, Label elseStart, Loc loc) implements Op {
    public Code code() {
        return Code.AddItem;
    }

    public String dump(final VM vm) {
        return "Branch rCondition: " + rCondition + " (" + vm.registers.get(rCondition).dump(vm) + ") " +
                "elseStart: " + elseStart;
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rCondition);
    }
}
