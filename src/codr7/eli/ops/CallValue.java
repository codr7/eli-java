package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record CallValue(IValue target, int rArgs, int arity, int rResult, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.CallValue;
    }

    @Override
    public String dump(final VM vm) {
        return "CallValue target: " + target.dump(vm) + " " +
                "rArgs: " + rArgs + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }
}
