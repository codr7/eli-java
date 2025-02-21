package codr7.eli.compilers;

import codr7.eli.Compiler;
import codr7.eli.VM;
import codr7.eli.ops.Copy;
import codr7.eli.ops.Nop;

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
                        if (cop2.rTo() == cop.rFrom()) {
                            final var frpc = vm.findWrite(cop.rFrom(), pc+1, pc);

                            if (frpc == null || frpc > rpc) {
                                vm.ops.set(pc, new Nop());
                                System.out.println("DELETE " + pc + " " + op.dump(vm));
                                vm.ops.set(rpc, new Nop());
                                System.out.println("DELETE " + rpc + " " + rop.dump(vm));
                                changed = true;
                            }
                        } else  if (vm.findRead(cop.rTo(), rpc+1, pc, rpc) == null &&
                            vm.findRead(cop2.rTo(), pc+1, pc, rpc) == null) {
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

        return changed;
    }
}