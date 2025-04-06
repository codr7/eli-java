package codr7.eli.ops;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;

public record MapSet(int rMap, int rItem, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "MapSet rMap: " + rMap + " (" + vm.registers.get(rMap).dump(vm) + ") " +
                "rItem: " + rItem + " (" + vm.registers.get(rItem).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var m = vm.registers.get(rMap).cast(CoreLib.Map);
        final var it = vm.registers.get(rItem);
        final var vs = new ArrayList<IValue>();
        Value.expand(vm, it, vs, loc);

        for (final var v: vs) {
            final var p = v.cast(CoreLib.Pair);
            m.put(p.left(), p.right());
        }

        vm.pc++;
    }
}