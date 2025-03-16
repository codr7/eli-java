package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record MapSet(int rMap, int rItem, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.MapSet;
    }

    @Override
    public String dump(final VM vm) {
        return "MapSet rMap: " + rMap + " (" + vm.registers.get(rMap).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }
}