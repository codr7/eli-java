package codr7.jx.libs.core.traits;

import codr7.jx.IValue;
import codr7.jx.Iter;
import codr7.jx.Loc;
import codr7.jx.VM;

public interface SeqTrait {
    Iter iter(VM vm, IValue target, Loc loc);
}
