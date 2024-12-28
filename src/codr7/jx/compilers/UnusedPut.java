package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Nop;
import codr7.jx.ops.Put;

import java.util.HashSet;
import java.util.Set;

import static codr7.jx.OpCode.PUT;

public record UnusedPut() implements Compiler {
    public static final UnusedPut instance = new UnusedPut();

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == PUT) {
                if (vm.findRead(((Put) op.data()).rTarget(), pc + 1, new HashSet<>(pc)) == null) {
                    System.out.println("Removing op: " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Nop.make(op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}