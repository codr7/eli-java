package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Next(int rIter, int rItem, Label bodyEnd, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Next;
    }

    @Override
    public String dump(final VM vm) {
        return "Next rIter: " + rIter + " (" + vm.registers.get(rIter).dump(vm) + ") " +
                "rItem: " + rItem + " (" + ((rItem == -1) ? "n/a" : vm.registers.get(rItem).dump(vm)) + ") " +
                "bodyEnd: " + bodyEnd;
    }
}
