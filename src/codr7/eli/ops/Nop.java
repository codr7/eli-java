package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

public record Nop() implements Op {
    @Override
    public Code code() {
        return Code.Nop;
    }

    @Override
    public String dump(VM vm) {
        return "Nop";
    }
}
