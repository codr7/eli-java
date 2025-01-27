import codr7.jx.REPL;
import codr7.jx.VM;

import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final var vm = new VM();
        vm.userLib.importFrom(vm.coreLib);

        if (args.length == 0) {
        } else {
            final var rResult = vm.alloc(1);
            final var start = vm.label();
            final var as = new ArrayDeque<>(Arrays.asList(args));

            while (!as.isEmpty()) {
                if (as.getFirst().equals("--debug")) {
                    as.removeFirst();
                    vm.debug = true;
                    continue;
                }

                if (as.getFirst().equals("--list")) {
                    as.removeFirst();

                    for (final var a : as) {
                        vm.load(Paths.get(a), rResult);
                    }

                    vm.dumpOps(start.pc);
                    return;
                }

                for (final var a : as) {
                    vm.load(Paths.get(a), rResult);
                }

                vm.eval(start.pc);
                return;
            }

            System.out.print("jx v" + VM.VERSION + "\n\n");
            new REPL(vm, System.in, System.out).run();
        }
    }
}