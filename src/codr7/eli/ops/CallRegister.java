package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record CallRegister(int rTarget, int rArguments, int arity, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.CallRegister;
    }

    @Override
    public String dump(final VM vm) {
        return "CallRegister rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rArgs: " + rArguments + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}
