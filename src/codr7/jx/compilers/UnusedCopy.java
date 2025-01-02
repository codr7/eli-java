package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Nop;

public record UnusedCopy() implements Compiler {
    public static final UnusedCopy instance = new UnusedCopy();

    public boolean compile(final VM vm, final int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op instanceof Copy cop) {
                final var rpc = vm.findRead(cop.rTo(), pc+1, pc);

                if (rpc == null) {
                    vm.ops.set(pc, new Nop());
                    System.out.println("DELETE " + pc + " " + op.dump(vm));
                    changed = true;
                } else {
                    final var rop = vm.ops.get(rpc);

                    if (rop instanceof Copy cop2) {
                        if (vm.findRead(cop.rTo(), rpc+1, pc, rpc) == null &&
                            vm.findRead(cop2.rTo(), pc+1, pc, rpc) == null) {
                            if (cop2.rTo() == cop.rFrom()) {
                                vm.ops.set(pc, new Nop());
                                System.out.println("DELETE " + pc + " " + op.dump(vm));
                                vm.ops.set(rpc, new Nop());
                                System.out.println("DELETE " + rpc + " " + rop.dump(vm));
                                changed = true;
                            } else {
                                final var uop = new Copy(cop.rFrom(), cop2.rTo(), cop.loc());
                                vm.ops.set(pc, uop);
                                System.out.println("UPDATE " + pc + " " + uop.dump(vm));
                                vm.ops.set(rpc, new Nop());
                                System.out.println("DELETE " + rpc + " " + rop.dump(vm));
                                changed = true;
                            }
                        }
                    }
                }
            }
        }

        return changed;
    }
}