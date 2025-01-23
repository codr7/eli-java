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
                        System.out.println("SKIP " + tpc + " " + op2.dump(vm));
                        tpc = gop2.target().pc;
                    } else { break; }
                }

                if (tpc != firstTarget.pc) {
                    gop.target().pc = tpc;
                    System.out.println("UPDATE " + pc + " " + gop.dump(vm));
                    changed = true;
                }
            }
        }

        return changed;
    }
}
