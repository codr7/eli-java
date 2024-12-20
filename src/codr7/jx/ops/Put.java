package codr7.jx.ops;

import codr7.jx.*;

public record Put(int rTarget, IValue value) {
    public static Op make(final int rTarget, final IValue value, final Location location) {
        return new Op(OpCode.PUT, new Put(rTarget, value), location);
    }

    public String toString(final VM vm) {
        return "rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "value: " + value.dump(vm);
    }
}
