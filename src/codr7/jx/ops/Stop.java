package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;

import java.util.Set;

public record Stop() implements Op {
    @Override public String dump(VM vm) { return "Stop"; }
    @Override public void io(VM vm, Set<Integer> read, Set<Integer> write) {}
}
