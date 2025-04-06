package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.errors.EvalError;

public record Check(int rValues, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "Check rValues: " + rValues +
                " (" + vm.registers.get(rValues).dump(vm) + "/" + vm.registers.get(rValues + 1).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        final var expected = vm.registers.get(rValues);
        final var actual = vm.registers.get(rValues + 1);

        if (!expected.eq(actual)) {
            throw new EvalError("Check failed; expected " +
                    expected.dump(vm) + ", actual: " + actual.dump(vm),
                    loc);
        }

        vm.pc++;
    }
}