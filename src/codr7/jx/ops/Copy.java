package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Copy(int rFrom, int rTo, Loc loc) implements Op {
    @Override public String dump(final VM vm) {
        return "COPY rFrom: " + rFrom + " (" + vm.registers.get(rFrom).dump(vm) + ") " +
                "rTo: " + rTo + " (" + vm.registers.get(rTo).dump(vm) + ")";
    }

    @Override public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rFrom);
        write.add(rTo);
    }
}
