package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record MapGet(int rMap, IValue key, int rItem, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.MapGet;
    }

    @Override
    public String dump(final VM vm) {
        return "MapAdd rMap: " + rMap + " (" + vm.registers.get(rMap).dump(vm) + ") " +
                "key: " + key.dump(vm) + ' ' +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }
}