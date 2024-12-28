package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.Op;
import codr7.jx.VM;
import codr7.jx.ops.Goto;

import static codr7.jx.OpCode.GOTO;
import static codr7.jx.OpCode.NOP;

public record GotoGoto() implements Compiler {
    public static final GotoGoto instance = new GotoGoto();

    public boolean compile(VM vm, int startPc) {
        var changed = false;

        for (var pc = startPc; pc < vm.ops.size(); pc++) {
            final var op = vm.ops.get(pc);

            if (op.code() == GOTO) {
                var oldPc = ((Goto)op.data()).pc();
                var gpc = oldPc;

                while (true) {
                    final var gop = vm.ops.get(gpc);
                    if (gop.code() == NOP) { gpc++; }
                    else if (gop.code() == GOTO) { gpc = ((Goto) gop.data()).pc(); }
                    else { break; }
                }

                if (gpc != oldPc) {
                    System.out.println("GOTO GOTO " + pc);
                    vm.ops.set(pc, Goto.make(gpc, op.loc()));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
