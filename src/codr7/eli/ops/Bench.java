package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record Bench(long reps, Label bodyEnd, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Bench;
    }

    @Override
    public String dump(final VM vm) {
        return "Bench reps: " + reps + " bodyEnd: " + bodyEnd + " rResult: " + rResult;
    }
}