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

    public void run() {
        final var inputBuffer = new StringBuilder();
        final var location = new Loc("REPL");
        var lineIndex = 0;

        for (;;) {
            out.printf("% 2d ", location.line() + lineIndex);

            String line;
            try { line = in.readLine(); }
            catch (final IOException e) { throw new RuntimeException(e); }

            if (line.isEmpty()) {
                try {
                    final IValue result = vm.eval(inputBuffer.toString(), location);
                    out.println(result.dump(vm));
                } catch (final Exception e) {
                    out.println(e.getMessage());
                } finally {
                    inputBuffer.setLength(0);
                    lineIndex = 0;
                }
            } else {
                inputBuffer.append(line);
                inputBuffer.append('\n');
                lineIndex++;
            }
        }
    }
}