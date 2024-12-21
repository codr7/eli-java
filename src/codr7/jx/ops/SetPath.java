package codr7.jx.ops;

import codr7.jx.Location;
import codr7.jx.Op;
import codr7.jx.OpCode;
import codr7.jx.VM;

import java.nio.file.Path;

public record SetPath(Path path) {
    public static Op make(final Path path, final Location location) {
        return new Op(OpCode.SET_PATH, new SetPath(path), location);
    }

    public String toString(final VM vm) { return "path: " + path; }
}