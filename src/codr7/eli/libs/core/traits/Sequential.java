package codr7.eli.libs.core.traits;

import codr7.eli.IValue;

public interface Sequential extends Iterable {
    IValue head(IValue target);

    IValue tail(IValue target);
}
