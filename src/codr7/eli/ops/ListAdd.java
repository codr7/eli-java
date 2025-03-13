package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record ListAdd(int rTarget, int rItem, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.ListAdd;
    }

    @Override
    public String dump(final VM vm) {
        return "ListAdd rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }
}