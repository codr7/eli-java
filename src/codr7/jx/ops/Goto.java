package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

public record Goto(int pc) {
    public static Op make(final int pc, final Location location) {
        return new Op(OpCode.GOTO, new Goto(pc), location);
    }

    public String toString(final VM vm) { return "pc: " + pc; }
}