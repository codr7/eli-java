package codr7.jx.readers;

import codr7.jx.IForm;
import codr7.jx.Loc;
import codr7.jx.forms.SplatForm;

public class SplatReader extends SuffixReader {
    public static final SplatReader instance = new SplatReader();

    public SplatReader() { super('*'); }

    @Override public IForm boxTarget(final IForm target, final Loc loc) {
        return new SplatForm(target, loc);
    }
}