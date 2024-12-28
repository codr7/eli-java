package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Goto;

import static codr7.jx.OpCode.GOTO;
import static codr7.jx.OpCode.NOP;

public record DoubleGoto() implements Compiler {
    public static final DoubleGoto instance = new DoubleGoto();

    public boolean compile(VM vm, int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == GOTO) {
                var oldPc = ((codr7.jx.ops.Goto)op.data()).pc();
                var gpc = oldPc;

                while (true) {
                    final var gop = vm.ops.get(gpc);
                    if (gop.code() == NOP) { gpc++; }
                    else if (gop.code() == GOTO) { gpc = ((Goto) gop.data()).pc(); }
                    else { break; }
                }

                if (gpc != oldPc) {
                    final var gop = vm.ops.get(gpc);
                    System.out.println("Removing op: " + gpc + " " + gop.dump(vm) + " " + gop.loc());
                    vm.ops.set(pc, Goto.make(gpc, op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
