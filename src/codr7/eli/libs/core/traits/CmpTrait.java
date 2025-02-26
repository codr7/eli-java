package codr7.eli.libs.core.traits;

import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.VM;

public interface CmpTrait {
    int cmp(IValue lhs, IValue rhs);
}
