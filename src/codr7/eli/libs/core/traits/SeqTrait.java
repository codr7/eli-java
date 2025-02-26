package codr7.eli.libs.core.traits;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;

public interface SeqTrait extends IterTrait {
    IValue head(IValue target);
    int len(IValue target);
    IValue tail(IValue target);
}
