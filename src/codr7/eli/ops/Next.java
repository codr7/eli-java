package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record Next(int rIter, int rItem, Label bodyEnd, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Next rIter: " + rIter + " (" + vm.registers.get(rIter).dump(vm) + ") " +
                "rItem: " + rItem + " (" + ((rItem == -1) ? "n/a" : vm.registers.get(rItem).dump(vm)) + ") " +
                "bodyEnd: " + bodyEnd;
    }

    @Override
    public void eval(final VM vm) {
        final var iter = vm.registers.get(rIter).cast(CoreLib.Iter);
        vm.pc = (iter.next(vm, rItem, loc)) ? vm.pc + 1 : bodyEnd.pc;
    }
}
