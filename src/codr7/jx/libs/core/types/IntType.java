package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.Value;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.core.traits.NumTrait;

public class IntType extends BaseType<Long> implements NumTrait {
    public IntType(final String id) { super(id); }

    @Override public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) + rhs.cast(this));
    }

    @Override public IValue div(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) / rhs.cast(this));
    }

    @Override public IValue mul(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) * rhs.cast(this));
    }

    @Override public IValue sub(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) - rhs.cast(this));
    }
}
