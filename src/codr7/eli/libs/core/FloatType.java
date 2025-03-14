package codr7.eli.libs.core;

import codr7.eli.*;

import java.math.BigDecimal;

public final class FloatType
        extends BaseType<BigDecimal>
        implements ComparableTrait, NumericTrait {
    public FloatType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).add(rhs.cast(this)));
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override
    public IValue div(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).divide(rhs.cast(this)));
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).toString();
    }

    @Override
    public IValue mul(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).multiply(rhs.cast(this)));
    }

    @Override
    public IValue sub(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).subtract(rhs.cast(this)));
    }

    @Override
    public IValue sub(final IValue v) {
        return new Value<>(this, v.cast(this).negate());
    }
}
