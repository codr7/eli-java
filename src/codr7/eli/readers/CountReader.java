package codr7.eli.readers;

import codr7.eli.IForm;
import codr7.eli.Loc;
import codr7.eli.forms.CallForm;
import codr7.eli.forms.IdForm;

public class CountReader extends PrefixReader {
    public static final CountReader instance = new CountReader();

    public CountReader() {
        super('#');
    }

    @Override
    public IForm boxTarget(final IForm target, final Loc loc) {
        return new CallForm(new IForm[]{new IdForm("seq/count", loc), target}, loc);
    }
}