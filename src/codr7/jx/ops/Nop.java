package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;

public record Nop() {
    public static Op make(final Location location) {
        return new Op(OpCode.NOP, null, location);
    }
}
