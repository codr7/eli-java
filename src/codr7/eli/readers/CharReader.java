package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.forms.LiteralForm;
import codr7.eli.libs.CoreLib;

import java.util.Deque;

public class CharReader implements Reader {
    public static final CharReader instance = new CharReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '\\') {
            return false;
        }
        final var floc = loc.dup();
        loc.update(in.pop());
        final var c = in.pop();
        out.addLast(new LiteralForm(new Value<>(CoreLib.Char, c), floc));
        return true;
    }
}