package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

public record ExtendGoto() implements Compiler {
    public static final ExtendGoto instance = new ExtendGoto();

    public boolean compile(VM vm, int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op instanceof Goto gop) {
                var firstTarget = gop.target();
                var tpc = firstTarget.pc;

                while (true) {
                    final var op2 = vm.ops.get(tpc);
                    if (op2 instanceof Nop) { tpc++; }
                    else if (op2 instanceof Goto gop2) {
                        System.out.println("DELETE " + tpc + " " + op2.dump(vm));
                        vm.ops.set(tpc, new Nop());
                        tpc = gop2.target().pc;
                        changed = true;
                    } else { break; }
                }

                if (tpc == pc+1) {
                    System.out.println("DELETE " + pc + " " + op.dump(vm));
                    vm.ops.set(pc, new Nop());
                    changed = true;
                } else if (tpc != firstTarget.pc) {
                    final var uop = new Goto(vm.label(tpc), gop.loc());
                    vm.ops.set(pc, uop);
                    System.out.println("UPDATE " + pc + " " + uop.dump(vm));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
