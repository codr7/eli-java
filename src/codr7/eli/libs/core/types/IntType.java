package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.CmpTrait;
import codr7.eli.libs.core.traits.NumTrait;

public final class IntType extends BaseType<Long> implements CmpTrait, NumTrait {
    public IntType(final String id) { super(id); }

    @Override
    public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, lhs.cast(this) + rhs.cast(this));
    }

    @Override
    public int cmp(final IValue lhs, final IValue rhs) {
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
    public IValue sub(final IValue v) { return new Value<>(this, -v.cast(this)); }
}
