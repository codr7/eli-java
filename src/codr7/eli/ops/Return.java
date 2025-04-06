package codr7.eli.ops;

import codr7.eli.Op;
import codr7.eli.VM;

public record Return() implements Op {
    @Override
    public String dump(VM vm) {
        return "Return";
    }

    @Override
    public void eval(final VM vm) {
        final var c = vm.endCall();

        if (c.rResult() != c.target().rResult) {
            vm.registers.set(c.rResult(), vm.registers.get(c.target().rResult));
        }

        vm.pc = c.returnPc();
    }
}