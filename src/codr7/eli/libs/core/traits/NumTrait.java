package codr7.eli.libs.core.traits;

import codr7.eli.IValue;

public interface NumTrait {
    IValue add(IValue lhs, IValue rhs);

    IValue div(IValue lhs, IValue rhs);

    IValue mul(IValue lhs, IValue rhs);

    IValue sub(IValue lhs, IValue rhs);

    IValue sub(IValue v);
}
