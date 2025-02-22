package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Check(int rValues, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Check;
    }

    @Override
    public String dump(final VM vm) {
        return "Check rValues: " + rValues +
                " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues+1).dump(vm) + ")";
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rValues);
        read.add(rValues+1);
    }
}