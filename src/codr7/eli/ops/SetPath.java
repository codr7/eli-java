package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.nio.file.Path;

public record SetPath(Path path) implements Op {
    @Override
    public String dump(final VM vm) {
        return "SetPath path: " + path;
    }

    @Override
    public void eval(final VM vm) {
        vm.path = path;
        vm.pc++;
    }
}