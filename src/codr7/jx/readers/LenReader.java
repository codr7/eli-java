package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.CallForm;
import codr7.jx.forms.IdForm;

import java.util.Deque;

public class LenReader extends PrefixReader {
    public static final LenReader instance = new LenReader();

    public LenReader() { super('#'); }

    @Override public IForm boxTarget(final IForm target, final Loc loc) {
        return new CallForm(new IForm[]{new IdForm("len", loc), target}, loc);
    }
}