package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Goto(int pc) {
    public static Op make(final int pc, final Loc loc) {
        return new Op(OpCode.GOTO, new Goto(pc), loc);
    }

    public Op relocate(final int deltaPc, final Loc loc) {
        return make(pc + deltaPc, loc);
    }

    public String toString(final VM vm) { return "pc: " + pc; }
}