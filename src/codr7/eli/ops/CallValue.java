package codr7.eli.ops;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;
import codr7.eli.libs.CoreLib;

import java.util.Set;

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

    @Override
    public void io(final VM vm, final Set<Integer> read, final Set<Integer> write) {
        for (var i = 0; i < arity; i++) {
            read.add(rArgs + i);
        }
        write.add(rResult);

        if (target.type() == CoreLib.Method) {
            final var m = target.cast(CoreLib.Method);
            for (var i = 0; i < m.args().length; i++) {
                read.add(m.rArgs() + i);
            }
            write.add(m.rResult());
        }
    }
}
