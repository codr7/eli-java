package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.nio.file.Path;
import java.util.Set;

public record SetPath(Path path) implements Op {
    @Override
    public Code code() {
        return Code.SetPath;
    }

    @Override
    public String dump(final VM vm) {
        return "SetPath path: " + path;
    }

    @Override
    public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}