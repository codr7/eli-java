package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;

import static codr7.jx.OpCode.GOTO;
import static codr7.jx.OpCode.NOP;

public record Goto() implements Compiler {
    public static final Goto instance = new Goto();

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
                    else if (gop.code() == GOTO) { gpc = ((codr7.jx.ops.Goto) gop.data()).pc(); }
                    else { break; }
                }

                if (gpc != oldPc) {
                    System.out.println("GOTO " + op.dump(vm) + " " + op.loc());
                    vm.ops.set(pc, codr7.jx.ops.Goto.make(gpc, op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
