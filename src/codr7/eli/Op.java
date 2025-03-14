package codr7.eli;

public interface Op {
    Code code();

    default Object data() {
        return this;
    }

    String dump(VM vm);

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
        Trace, TypeCheck,
        Unzip,
        Zip
    }
}