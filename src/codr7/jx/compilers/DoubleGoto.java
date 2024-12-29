package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

import static codr7.jx.OpCode.GOTO;
import static codr7.jx.OpCode.NOP;

public record DoubleGoto() implements Compiler {
    public static final DoubleGoto instance = new DoubleGoto();

    public boolean compile(VM vm, int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == GOTO) {
                var firstTargetPc = ((codr7.jx.ops.Goto)op.data()).pc();
                var tpc = firstTargetPc;

                while (true) {
                    final var gop = vm.ops.get(tpc);
                    if (gop.code() == NOP) { tpc++; }
                    else if (gop.code() == GOTO) {
                        System.out.println("Skipping: " + tpc + " " + gop.dump(vm) + " " + gop.loc());
                        vm.ops.set(tpc, Nop.make(op.loc()));
                        tpc = ((Goto) gop.data()).pc();
                    } else { break; }
                }

                if (tpc != firstTargetPc) {
                    System.out.println("Extending: " + pc + " " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, Goto.make(tpc, op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
