package codr7.eli.ops;

import codr7.eli.*;

import java.util.Set;

public record Bench(Label bodyEnd, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.Bench;
    }

    @Override
    public String dump(final VM vm) {
        return "Bench bodyEnd: " + bodyEnd + " rResult: " + rResult;
    }

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        write.add(rResult);
    }
}