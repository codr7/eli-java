package codr7.eli.libs.core.traits;

import codr7.eli.IValue;

public interface SeqTrait extends IterableTrait {
    IValue head(IValue target);
    int len(IValue target);
    IValue tail(IValue target);
}
