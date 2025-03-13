package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Trace(String text) implements Op {
    @Override
    public Code code() {
        return Code.Trace;
    }

    @Override
    public Object data() {
        return text;
    }

    @Override
    public String dump(final VM vm) {
        return text;
    }
}