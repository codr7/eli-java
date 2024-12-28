package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record Put(int rTarget, IValue value) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        write.add(rTarget);
    }

    public static Op make(final int rTarget, final IValue value, final Loc loc) {
        return new Op(OpCode.PUT, new Put(rTarget, value), loc);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "value: " + value.dump(vm);
    }
}
