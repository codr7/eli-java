package codr7.jx.readers;

import codr7.jx.IForm;
import codr7.jx.Loc;
import codr7.jx.forms.CallForm;
import codr7.jx.forms.IdForm;

public class UnquoteReader extends PrefixReader {
    public static final UnquoteReader instance = new UnquoteReader();

    public UnquoteReader() { super(','); }

    @Override public IForm boxTarget(final IForm target, final Loc loc) {
        return new CallForm(new IForm[]{new IdForm("unquote", loc), target}, loc);
    }
}