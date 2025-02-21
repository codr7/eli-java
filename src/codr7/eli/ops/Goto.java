package codr7.eli.ops;

import codr7.eli.*;

import java.util.Set;

public record Goto(Label target) implements Op {
    public Code code() {
        return Code.Goto;
    }

    @Override public String dump(final VM vm) {
        return "Goto target: " + target;
    }

    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}