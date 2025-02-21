package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.nio.file.Path;
import java.util.Set;

public record SetPath(Path path) implements Op {
    public Code code() {
        return Code.SetPath;
    }

    @Override public String dump(final VM vm) {
        return "SetPath path: " + path;
    }

    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}