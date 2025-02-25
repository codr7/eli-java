import codr7.eli.REPL;
import codr7.eli.VM;

import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final var vm = new VM();
        vm.userLib.importFrom(vm.coreLib);

        if (args.length == 0) {
            System.out.print("eli v" + VM.VERSION + "\n\n");
            new REPL(vm, System.in, System.out).run();
        } else {
            final var startPc = vm.emitPc();
            final var rResult = vm.alloc(1);
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

                    vm.dumpOps(startPc);
                    return;
                }

                break;
            }

            for (final var a : as) {
                vm.load(Paths.get(a), rResult);
            }

            vm.eval(startPc);
        }
    }
}