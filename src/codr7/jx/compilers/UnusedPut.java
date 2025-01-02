package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Nop;
import codr7.jx.ops.Put;

public record UnusedPut() implements Compiler {
    public static final UnusedPut instance = new UnusedPut();

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op instanceof Put putOp) {
                final var rTarget = putOp.rTarget();

                if (vm.findRead(rTarget, pc+1, pc) == null) {
                    vm.ops.set(pc, new Nop());
                    System.out.println("DELETE " + pc + " " + op.dump(vm));
                    changed = true;
                }
            }
        }

        return changed;
    }
}