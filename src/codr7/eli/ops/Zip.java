package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Zip(int rLeft, int rRight, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Zip;
    }

    @Override
    public String dump(final VM vm) {
        return "Zip rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}