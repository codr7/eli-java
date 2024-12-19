package codr7.jx;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class REPL {
    private final BufferedReader in;
    private final PrintStream out;
    private final VM vm;

    public REPL(final VM vm, final InputStream in, final PrintStream out) {
        this.vm = vm;
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
    }

    public void run() throws IOException {
        final var inputBuffer = new StringBuilder();

        for (;;) {
            out.print("  ");
            final var line = in.readLine();

            if (line.isEmpty()) {
                try {
                    final IValue result = vm.eval(inputBuffer.toString());
                    out.println(result.dump(vm));
                } catch (final Exception e) {
                    out.println(e.getMessage());
                } finally {
                    inputBuffer.setLength(0);
                }
            } else {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
        }
    }
}