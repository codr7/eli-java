package codr7.eli.ops;

import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

import java.util.Set;

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

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        for (var i = 0; i < arity; i++) {
            read.add(rArguments + i);
        }
        write.add(rResult);

        final var t = vm.registers.get(rTarget);

        if (t.type() == CoreLib.methodType) {
            final var m = t.cast(CoreLib.methodType);
            for (var i = 0; i < m.args().length; i++) {
                read.add(m.rArgs() + i);
            }
            write.add(m.rResult());
        }
    }
}
