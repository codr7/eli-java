package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record MapAdd(int rTarget, int rKey, int rValue, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.MapAdd;
    }

    @Override
    public String dump(final VM vm) {
        return "MapAdd rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rKey: " + rKey + " (" + vm.registers.get(rKey).dump(vm) + ") " +
                "rValue: " + rValue + " (" + vm.registers.get(rValue).dump(vm) + ")";
    }
}