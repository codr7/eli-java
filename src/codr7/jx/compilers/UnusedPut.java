package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Nop;
import codr7.jx.ops.Put;

import java.util.HashSet;

import static codr7.jx.OpCode.COPY;
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
                final var skip = new HashSet<Integer>();
                skip.add(pc);
                final var rpc = vm.findRead(rTarget, pc+1, skip);

                if (rpc == null) {
                    System.out.println("Removing: " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Nop.make(op.loc()));
                    changed = true;
                } else {
                    final var rop = vm.ops.get(rpc);

                    if (rop.code() == COPY) {
                        final var rTo = ((Copy)rop.data()).rTo();

                        skip.clear();
                        skip.add(rpc);
                        skip.add(pc);

                        final var postRpc = vm.findRead(rTarget, 0, skip);

                        if (postRpc == null) {
                            System.out.println("Removing: " + rpc + " " + rop.dump(vm) + " " + rop.loc());
                            vm.ops.set(rpc, Nop.make(rop.loc()));
                            vm.ops.set(pc, Put.make(rTo, putOp.value(), op.loc()));
                            System.out.println("Updated: " + pc + " " + vm.ops.get(pc).dump(vm) + " " + op.loc());
                        }
                    }
                }
            }
        }

        return changed;
    }
}