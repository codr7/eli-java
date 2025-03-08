package codr7.eli.libs.core.traits;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.VM;

public interface Iterable {
    Iter iter(VM vm, IValue target);
}