package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

import static codr7.jx.OpCode.GOTO;
import static codr7.jx.OpCode.NOP;

public record ExtendGoto() implements Compiler {
    public static final ExtendGoto instance = new ExtendGoto();

    public boolean compile(VM vm, int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == GOTO) {
                var firstTarget = ((Goto)op.data()).target();
                var tpc = firstTarget.pc;

                while (true) {
                    final var gop = vm.ops.get(tpc);
                    if (gop.code() == NOP) { tpc++; }
                    else if (gop.code() == GOTO) {
                        System.out.println("DELETE " + tpc + " " + gop.dump(vm) + " " + gop.loc());
                        vm.ops.set(tpc, Nop.make(op.loc()));
                        tpc = ((Goto) gop.data()).target().pc;
                        changed = true;
                    } else { break; }
                }

                if (tpc == pc+1) {
                    System.out.println("DELETE " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Nop.make(op.loc()));
                    changed = true;
                } else if (tpc != firstTarget.pc) {
                    final var uop = Goto.make(vm.label(tpc), op.loc());
                    vm.ops.set(pc, uop);
                    System.out.println("UPDATE " + pc + " " + uop.dump(vm) + " " + uop.loc());
                    changed = true;
                }
            }
        }

        return changed;
    }
}
