package codr7.jx.ops;

import codr7.jx.Loc;
import codr7.jx.Op;
import codr7.jx.VM;
import codr7.jx.libs.Core;

import java.util.Set;

public record CallRegister(int rTarget, int rArguments, int arity, int rResult, Loc loc) implements Op {
    public String dump(final VM vm) {
        return "CALL_REGISTER rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "rArgs: " + rArguments + " " +
                "arity: " + arity + " " +
                "rResult: " + rResult + " (" + vm.registers.get(rResult).dump(vm) + ")";
    }

    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        read.add(rTarget);
        for (var i = 0; i < arity; i++) { read.add(rArguments+i); }
        write.add(rResult);

        final var t = vm.registers.get(rTarget);

        if (t.type() == Core.methodType) {
            final var m = t.cast(Core.methodType);
            for (var i = 0; i < m.args().length; i++) { read.add(m.rArgs()+i); }
            write.add(m.rResult());
        }
    }
}
