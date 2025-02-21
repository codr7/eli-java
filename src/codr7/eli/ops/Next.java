package codr7.eli.ops;

import codr7.eli.*;

import java.util.Set;

public record Next(int rIter, int rItem, Label bodyEnd, Loc loc) implements Op {
    public Code code() {
        return Code.Next;
    }

    public String dump(final VM vm) {
        return "Next rIter: " + rIter + " (" + vm.registers.get(rIter).dump(vm) + ") " +
                "rItem: " + rItem + " (" + ((rItem == -1) ? "n/a" : vm.registers.get(rItem).dump(vm)) + ") " +
                "bodyEnd: " + bodyEnd;
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rIter);
        write.add(rItem);
    }
}
