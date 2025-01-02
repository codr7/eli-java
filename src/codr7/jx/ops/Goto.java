package codr7.jx.ops;

import codr7.jx.*;

import java.util.Set;

public record Goto(Label target, Loc loc) implements Op {
    @Override public String dump(final VM vm) { return "GOTO target: " + target; }
    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}