package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record CallValue(IValue target, int rArgs, int arity, int rResult, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "CallValue target: " + target.dump(vm) + " " +
                "rArgs: " + rArgs + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        var t = target;

        if (t.type() == CoreLib.Binding) {
            t = vm.registers.get(t.cast(CoreLib.Binding).rValue());
        }

        vm.pc++;
        t.type().cast(CoreLib.Callable, loc).call(vm, t, rArgs, arity, rResult, false, loc);
    }
}
