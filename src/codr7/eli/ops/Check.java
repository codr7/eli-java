package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Check(int rValues, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Check;
    }

    @Override
    public String dump(final VM vm) {
        return "Check rValues: " + rValues +
                " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues + 1).dump(vm) + ")";
    }
}