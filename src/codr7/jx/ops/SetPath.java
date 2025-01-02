package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.nio.file.Path;
import java.util.Set;

public record SetPath(Path path, Loc loc) implements Op {
    @Override public String dump(final VM vm) { return "SET_PATH path: " + path; }
    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}