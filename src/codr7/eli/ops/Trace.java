package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

public record Trace(String text) implements Op {
    @Override
    public String dump(final VM vm) {
        return text;
    }

    @Override
    public void eval(final VM vm) {
        System.out.println(text);
        vm.pc++;
    }
}