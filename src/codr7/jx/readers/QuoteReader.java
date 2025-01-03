package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.QuoteForm;

import java.util.Deque;

public class QuoteReader implements Reader {
    public static final QuoteReader instance = new QuoteReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '\'') { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        if (!vm.read(in, out, loc)) { throw new ReadError("Invalid quote", loc); }
        out.addLast(new QuoteForm(out.removeLast(), loc));
        return true;
    }
}