package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;

public record Call(int rTarget, int rParams, int arity, int rResult) {
    public static Op make(final int rTarget,
                          final int rParams,
                          final int arity,
                          final int rResult,
                          final Location location) {
        return new Op(OpCode.CALL, new Call(rTarget, arity, rParams, rResult), location);
    }
}
