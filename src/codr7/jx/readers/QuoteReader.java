package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.QuoteForm;

import java.util.Deque;

public class QuoteReader extends PrefixReader {
    public static final QuoteReader instance = new QuoteReader();

    public QuoteReader() { super('\''); }

    @Override public IForm boxTarget(final IForm target, final Loc loc) {
        return new QuoteForm(target, loc);
    }
}