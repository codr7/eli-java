package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Unzip(int rPair, int rLeft, int rRight, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Unzip;
    }

    @Override
    public String dump(final VM vm) {
        return "Unzip rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")";
    }
}