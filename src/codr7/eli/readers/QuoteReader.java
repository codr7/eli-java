package codr7.eli.readers;

import codr7.eli.IForm;
import codr7.eli.Loc;
import codr7.eli.forms.QuoteForm;

public class QuoteReader extends PrefixReader {
    public static final QuoteReader instance = new QuoteReader();

    public QuoteReader() {
        super('\'');
    }

    @Override
    public IForm boxTarget(final IForm target, final Loc loc) {
        return new QuoteForm(target, loc);
    }
}