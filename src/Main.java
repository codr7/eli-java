import codr7.jx.REPL;
import codr7.jx.VM;

public class Main {
    public static void main(String[] args) {
        final var vm = new VM();
        System.out.print("jx v" + VM.VERSION + "\n\n");
        new REPL(vm, System.in, System.out).run();
    }
}