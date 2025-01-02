package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Nop;
import codr7.jx.ops.Put;

import static codr7.jx.OpCode.PUT;

public record UnusedPut() implements Compiler {
    public static final UnusedPut instance = new UnusedPut();

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == PUT) {
                final var putOp = (Put) op.data();
                final var rTarget = putOp.rTarget();

                if (vm.findRead(rTarget, pc+1, pc) == null) {
                    vm.ops.set(pc, Nop.make(op.loc()));
                    System.out.println("DELETE " + pc + " " + op.dump(vm) + " " + op.loc());
                    changed = true;
                }
            }
        }

        return changed;
    }
}