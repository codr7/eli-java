package codr7.jx.libs.core.traits;

import codr7.jx.IValue;

public interface NumTrait {
    IValue add(IValue lhs, IValue rhs);
    IValue div(IValue lhs, IValue rhs);
    IValue mul(IValue lhs, IValue rhs);
    IValue sub(IValue lhs, IValue rhs);
}
