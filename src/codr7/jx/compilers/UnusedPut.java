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

    public static boolean findRead(final VM vm, final int rTarget, final int startPc, final Set<Integer> skip) {
        final var r = new HashSet<Integer>();
        final var w = new HashSet<Integer>();
        for (var pc = startPc; pc < vm.ops.size();) {
            if (skip.contains(pc)) { return false; }
            skip.add(pc);
            final var op = vm.ops.get(pc);
            op.io(vm, r, w);
            if (r.contains(rTarget)) { return true; }
            if (w.contains(rTarget)) { break; }

            if (op.data() == null) {
                pc ++;
            } else {
                switch (op.data()) {
                    case codr7.jx.ops.Goto gotoOp: {
                        if (findRead(vm, rTarget, pc + 1, skip)) { return true; }
                        pc = gotoOp.pc();
                        break;
                    }
                    default:
                        pc++;
                        break;
                }
            }
        }

        return false;
    }

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == PUT) {
                if (!findRead(vm, ((Put) op.data()).rTarget(), pc + 1, new HashSet<>(pc))) {
                    System.out.println("Unused PUT: " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Nop.make(op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}