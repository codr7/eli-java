package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

public record CallRegister(int rTarget, int rArguments, int arity, int rResult, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "CallRegister rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rArgs: " + rArguments + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    @Override
    public void eval(final VM vm) {
        var t = vm.registers.get(rTarget);

        if (t.type() == CoreLib.Binding) {
            t = vm.registers.get(t.cast(CoreLib.Binding).rValue());
        }

        vm.pc++;
        t.type().cast(CoreLib.Callable, loc).call(vm, t, rArguments, arity, rResult, false, loc);
    }
}
