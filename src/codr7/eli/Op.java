package codr7.eli;

import java.util.Set;

public interface Op {
    Code code();

    default Object data() {
        return this;
    }

    String dump(VM vm);

    void io(VM vm, Set<Integer> read, Set<Integer> write);

    enum Code {
        Bench, Branch,
        CallRegister, CallValue, Check, Copy,
        Goto,
        Inc, Iter,
        Left, ListAdd,
        MapAdd,
        Next, Nop,
        Put,
        Return, Right,
        SetPath, Splat, Stop,
        Trace,
        Unzip,
        Zip
    }
}