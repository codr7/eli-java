package codr7.eli.libs.core;

import codr7.eli.IValue;

public interface NumericTrait extends ComparableTrait {
    IValue add(IValue lhs, IValue rhs);

    IValue div(IValue lhs, IValue rhs);

    IValue mul(IValue lhs, IValue rhs);

    IValue sub(IValue lhs, IValue rhs);

    IValue sub(IValue v);
}
