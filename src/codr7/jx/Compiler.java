package codr7.jx;

import java.util.Deque;

public interface Compiler {
    boolean compile(VM vm, int startPc);
}