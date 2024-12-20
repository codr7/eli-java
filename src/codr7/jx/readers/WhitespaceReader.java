package codr7.jx.readers;

import codr7.jx.*;

import java.util.Deque;

public class WhitespaceReader implements Reader {
    public static final WhitespaceReader instance = new WhitespaceReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Location location) {
        for (; ; ) {
            final var c = in.peek();
            if (!Character.isWhitespace(c)) { break; }
            in.pop();
            location.update(c);
        }

        return false;
    }
}