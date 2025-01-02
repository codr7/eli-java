package codr7.jx;

import codr7.jx.ops.*;

import java.util.Set;

public interface Op {
    String dump(VM vm);
    void io(VM vm, Set<Integer> read, Set<Integer> write);
}