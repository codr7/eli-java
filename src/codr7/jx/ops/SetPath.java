package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.nio.file.Path;

public record SetPath(Path path) {
    public static Op make(final Path path, final Loc loc) {
        return new Op(OpCode.SET_PATH, new SetPath(path), loc);
    }

    public String toString(final VM vm) { return "path: " + path; }
}