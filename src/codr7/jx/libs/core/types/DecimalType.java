package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.libs.core.traits.CmpTrait;
import codr7.jx.libs.core.traits.NumTrait;

import java.math.BigDecimal;

public class DecimalType extends BaseType<BigDecimal> implements CmpTrait, NumTrait {
    public DecimalType(final String id) { super(id); }

    @Override public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).add(rhs.cast(this)));
    }

    @Override public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override public IValue div(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).divide(rhs.cast(this)));
    }

    @Override public String dump(final VM vm, final IValue value) {
        return value.cast(this).toString();
    }

    @Override public IValue mul(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).multiply(rhs.cast(this)));
    }

    @Override public IValue sub(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this).subtract(rhs.cast(this)));
    }

    @Override public IValue sub(final IValue v) {
        return new Value<>(this, v.cast(this).negate());
    }
}
