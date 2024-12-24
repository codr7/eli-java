package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;

public record Nop() {
    public static Op make(final Loc loc) {
        return new Op(OpCode.NOP, null, loc);
    }
}
