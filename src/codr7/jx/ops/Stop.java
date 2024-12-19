package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;

public record Stop() {
    public static Op make(final int rFrom, final int rTo, final Location location) {
        return new Op(OpCode.STOP, null, location);
    }
}
