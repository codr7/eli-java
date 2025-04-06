package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record Unzip(int rPair, int rLeft, int rRight, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Unzip rPair: " + rPair + " (" + vm.registers.get(rPair).dump(vm) + ") " +
                "rLeft: " + rLeft + " (" + vm.registers.get(rLeft).dump(vm) + ") " +
                "rRight: " + rRight + " (" + vm.registers.get(rRight).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var p = vm.registers.get(rPair).cast(CoreLib.Pair);
        vm.registers.set(rLeft, p.left());
        vm.registers.set(rRight, p.right());
        vm.pc++;
    }
}