package codr7.eli.readers;

import codr7.eli.IForm;
import codr7.eli.Loc;
import codr7.eli.forms.SplatForm;

public class SplatReader extends SuffixReader {
    public static final SplatReader instance = new SplatReader();

    public SplatReader() {
        super('*');
    }

    @Override
    public IForm boxTarget(final IForm target, final Loc loc) {
        return new SplatForm(target, loc);
    }
}