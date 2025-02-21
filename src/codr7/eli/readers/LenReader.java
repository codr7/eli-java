package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.forms.CallForm;
import codr7.eli.forms.IdForm;

public class LenReader extends PrefixReader {
    public static final LenReader instance = new LenReader();

    public LenReader() { super('#'); }

    @Override public IForm boxTarget(final IForm target, final Loc loc) {
        return new CallForm(new IForm[]{new IdForm("len", loc), target}, loc);
    }
}