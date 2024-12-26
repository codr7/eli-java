package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Bench(int endPc, int rResult) {
    public static Op make(final int endPc, final int rResult, final Loc loc) {
        return new Op(OpCode.BENCH, new Bench(endPc, rResult), loc);
    }

    public Op relocate(final int deltaPc, final Loc loc) {
        return make(endPc + deltaPc, rResult, loc);
    }

    public String toString(final VM vm) { return "endPc: " + endPc + " rResult: " + rResult; }
}