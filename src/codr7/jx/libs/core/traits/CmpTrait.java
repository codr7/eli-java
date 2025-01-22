package codr7.jx.libs.core.traits;

import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;

public interface CmpTrait {
    int cmp(VM vm, IValue lhs, IValue rhs, Loc loc);
}
