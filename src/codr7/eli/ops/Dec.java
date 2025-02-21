package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Dec(int rTarget, int rDelta, Loc loc) implements Op {
    public Code code() {
        return Code.Dec;
    }

    public String dump(final VM vm) {
        return "Dec rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rDelta: " + rDelta + " (" + ((rDelta == -1) ? "?" : vm.registers.get(rDelta).dump(vm)) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        write.add(rTarget);
        if (rDelta != -1) { read.add(rDelta); }
    }
}
