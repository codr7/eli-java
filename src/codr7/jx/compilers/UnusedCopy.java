package codr7.jx.compilers;

import codr7.jx.Compiler;
import codr7.jx.VM;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

import java.util.HashSet;

import static codr7.jx.OpCode.COPY;

    public record UnusedCopy() implements Compiler {
        public static final UnusedCopy instance = new UnusedCopy();

        public boolean compile(VM vm, int startPc) {
            var changed = false;

            for (var pc = startPc; pc < vm.ops.size(); pc++) {
                final var op = vm.ops.get(pc);

                if (op.code() == COPY) {
                    final var rTo = ((Copy)op.data()).rTo();
                    final var r = new HashSet<Integer>();
                    final var w = new HashSet<Integer>();
                    var used = false;

                    for (var cpc = pc+1; cpc < vm.ops.size(); cpc++) {
                        final var cop = vm.ops.get(cpc);
                        cop.io(r, w);

                        if (r.contains(rTo)) {
                            used = true;
                            break;
                        } else if (w.contains(rTo)) {
                            break;
                        }
                    }

                    if (!used) {
                        System.out.println("Unused COPY " + pc);
                        vm.ops.set(pc, Nop.make(op.loc()));
                        changed = true;
                    }
                }
            }

            return changed;
        }
    }