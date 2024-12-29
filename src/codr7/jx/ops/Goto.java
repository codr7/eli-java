package codr7.jx.ops;

import codr7.jx.*;

public record Goto(Label target) {
    public static Op make(final Label target, final Loc loc) {
        return new Op(OpCode.GOTO, new Goto(target), loc);
    }

    public String toString(final VM vm) { return "target: " + target; }
}