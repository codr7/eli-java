package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record Bench(Label bodyEnd, int rResult) {
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        write.add(rResult);
    }

    public static Op make(final Label bodyEnd, final int rResult, final Loc loc) {
        return new Op(OpCode.BENCH, new Bench(bodyEnd, rResult), loc);
    }

    public String toString(final VM vm) { return "bodyEnd: " + bodyEnd + " rResult: " + rResult; }
}