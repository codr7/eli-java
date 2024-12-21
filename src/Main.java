import codr7.jx.REPL;
import codr7.jx.VM;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        final var vm = new VM();
        vm.userLib.importFrom(vm.coreLib);

        if (args.length == 0) {
            System.out.print("jx v" + VM.VERSION + "\n\n");
            new REPL(vm, System.in, System.out).run();
        } else {
            final var rResult = vm.alloc(1);
            final var startPc = vm.emitPc();
            for (final var a: args) { vm.load(Paths.get(a), rResult); }
            vm.eval(startPc);
        }
    }
}