package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.Value;
import codr7.eli.libs.core.traits.ComparableTrait;
import codr7.eli.libs.core.traits.NumericTrait;

public final class IntType extends BaseType<Long> implements ComparableTrait, NumericTrait {
    public IntType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) + rhs.cast(this));
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override
    public IValue div(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) / rhs.cast(this));
    }

    @Override
    public IValue mul(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) * rhs.cast(this));
    }

    @Override
    public IValue sub(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) - rhs.cast(this));
    }

    @Override
    public IValue sub(final IValue v) {
        return new Value<>(this, -v.cast(this));
    }
}
