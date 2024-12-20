package codr7.jx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class Input {
    private final BufferedReader in;
    private final Deque<Character> buffer = new ArrayDeque<>();

    public Input(final java.io.Reader in) {
        this.in = new BufferedReader(in);
    }

    public Input(final String in) {
        this(new StringReader(in));
    }
    public char peek() {
        if (buffer.isEmpty()) {
            fillBuffer();

            if (buffer.isEmpty()) {
                return 0;
            }
        }

        return buffer.getFirst();
    }

    public char pop() {
        if (buffer.isEmpty()) {
            fillBuffer();

            if (buffer.isEmpty()) {
                return 0;
            }
        }

        return buffer.removeFirst();
    }

    public void push(char c) { buffer.addFirst(c); }

    private void fillBuffer() {
        try {
            final var cp = in.read();

            if (cp != -1) {
                for (final var c : Character.toChars(cp)) {
                    buffer.addLast(c);
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
