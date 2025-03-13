package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Right(int rPair, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Right;
    }

    @Override
    public String dump(final VM vm) {
        return "Right rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}