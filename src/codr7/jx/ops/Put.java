package codr7.jx.ops;

import codr7.jx.IValue;
import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;

public record Put(int rTarget, IValue value) {
    public static Op make(final int rTarget, final IValue value, final Location location) {
        return new Op(OpCode.PUT, new Put(rTarget, value), location);
    }
}
