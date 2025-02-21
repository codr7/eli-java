package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Nop() implements Op {
    public Code code() {
        return Code.Nop;
    }

    @Override public String dump(VM vm) {
        return "Nop";
    }

    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}
