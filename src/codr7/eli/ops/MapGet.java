package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record MapGet(int rMap, IValue key, int rItem, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "MapAdd rMap: " + rMap + " (" + vm.registers.get(rMap).dump(vm) + ") " +
                "key: " + key.dump(vm) + ' ' +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var m = vm.registers.get(rMap).cast(CoreLib.Map);
        vm.registers.set(rItem, m.get(key));
        vm.pc++;
    }
}