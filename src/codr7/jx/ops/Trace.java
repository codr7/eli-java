package codr7.jx.ops;

import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Trace(String text) implements Op {
    @Override public String dump(final VM vm) { return text; }
    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}