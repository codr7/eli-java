package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record Goto(Label target) implements Op {
    @Override
    public Code code() {
        return Code.Goto;
    }

    @Override
    public Object data() {
        return target;
    }

    @Override
    public String dump(final VM vm) {
        return "Goto target: " + target;
    }
}