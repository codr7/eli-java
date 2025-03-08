package codr7.eli.libs.core.traits;

import codr7.eli.IValue;

public interface SequentialTrait extends IterableTrait {
    IValue head(IValue target);

    IValue tail(IValue target);
}
