package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

public record Return() implements Op {
    @Override
    public Code code() {
        return Code.Return;
    }

    @Override
    public String dump(VM vm) {
        return "Return";
    }
}