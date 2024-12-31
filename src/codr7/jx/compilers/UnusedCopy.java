package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Nop;

import java.util.HashSet;

import static codr7.jx.OpCode.COPY;

public record UnusedCopy() implements Compiler {
    public static final UnusedCopy instance = new UnusedCopy();

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == COPY) {
                if (vm.findRead(((Copy) op.data()).rTo(), pc + 1, new HashSet<>(pc)) == null) {
                    System.out.println("DELETE " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Nop.make(op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}