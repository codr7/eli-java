package codr7.eli.ops;

import codr7.eli.Label;
import codr7.eli.Op;
import codr7.eli.VM;

public record Goto(Label target) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Goto target: " + target;
    }

    @Override
    public void eval(final VM vm) {
        vm.pc = target.pc;
    }
}