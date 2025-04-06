package codr7.eli.ops;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

public record Zip(int rLeft, int rRight, int rResult, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Zip rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ") " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var l = vm.registers.get(rLeft);
        final var r = vm.registers.get(rRight);
        vm.registers.set(rResult, new Value<>(CoreLib.Pair, new Pair(l, r)));
        vm.pc++;
    }
}