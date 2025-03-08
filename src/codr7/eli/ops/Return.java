package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Return() implements Op {
    @Override
    public Code code() {
        return Code.Return;
    }

    @Override
    public String dump(VM vm) {
        return "Return";
    }

    @Override
    public void io(VM vm, Set<Integer> read, Set<Integer> write) {
    }
}