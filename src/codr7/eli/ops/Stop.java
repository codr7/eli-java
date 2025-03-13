package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Stop() implements Op {
    @Override
    public Code code() {
        return Code.Stop;
    }

    @Override
    public String dump(VM vm) {
        return "Stop";
    }
}
