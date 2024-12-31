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
                final var cop = ((Copy) op.data());
                final var skip = new HashSet<Integer>();
                skip.add(pc);
                final var rpc = vm.findRead(cop.rTo(), pc+1, skip);

                if (rpc == null) {
                    vm.ops.set(pc, Nop.make(op.loc()));
                    System.out.println("DELETE " + pc + " " + op.dump(vm) + " " + op.loc());
                    changed = true;
                } else {
                    final var rop = vm.ops.get(rpc);

                    if (rop.code() == COPY) {
                        final var cop2 = ((Copy) rop.data());
                        skip.clear();
                        skip.add(pc);
                        skip.add(rpc);

                        if (vm.findRead(cop.rTo(), rpc+1, skip) == null) {
                            if (cop2.rTo() == cop.rFrom()) {
                                vm.ops.set(pc, Nop.make(rop.loc()));
                                System.out.println("DELETE " + pc + " " + op.dump(vm) + " " + op.loc());
                                vm.ops.set(rpc, Nop.make(rop.loc()));
                                System.out.println("DELETE " + rpc + " " + rop.dump(vm) + " " + rop.loc());
                                changed = true;
                            } else {
                                /*final var uop = Copy.make(cop.rFrom(), ((Copy) rop.data()).rTo(), op.loc());
                                vm.ops.set(pc, uop);
                                System.out.println("!UPDATE " + pc + " " + uop.dump(vm) + " " + uop.loc());
                                vm.ops.set(rpc, Nop.make(rop.loc()));
                                System.out.println("!DELETE " + rpc + " " + rop.dump(vm) + " " + rop.loc());
                                changed = true;*/
                            }
                        }
                    }
                }
            }
        }

        return changed;
    }
}