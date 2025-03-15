package codr7.eli.libs.core;

import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.VM;

public interface IterableTrait {
    Iter iter(VM vm, IValue target);
}