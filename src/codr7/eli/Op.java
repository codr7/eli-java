package codr7.eli;

import java.util.Set;

public interface Op {
    enum Code {
        AddItem,
        Bench, Branch,
        CallRegister, CallValue, Check, Copy,
        Dec,
        Goto,
        Inc, Iter,
        Left,
        MakeList,
        Next, Nop,
        Put,
        Return, Right,
        SetPath, Splat, Stop,
        Trace,
        Unzip,
        Zip
    }

    Code code();
    String dump(VM vm);
    void io(VM vm, Set<Integer> read, Set<Integer> write);
}