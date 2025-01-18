package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.LiteralForm;
import codr7.jx.forms.QuoteForm;
import codr7.jx.libs.CoreLib;

import java.util.Deque;

public class CharReader implements Reader {
    public static final CharReader instance = new CharReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '\\') { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        final var c = in.pop();
        out.addLast(new LiteralForm(new Value<>(CoreLib.charType, c), floc));
        return true;
    }
}