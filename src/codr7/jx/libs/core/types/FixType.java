package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.core.traits.NumTrait;

public class FixType extends BaseType<Long> implements NumTrait {
    public FixType(final String id) { super(id); }

    @Override public IValue add(final IValue lhs, final IValue rhs) {
        return new Value<>(this, Fix.add(lhs.cast(this), rhs.cast(this)));
    }

    @Override public IValue div(final IValue lhs, final IValue rhs) {
        return new Value<>(this, Fix.div(lhs.cast(this), rhs.cast(this)));
    }

    @Override public String dump(final VM vm, final IValue value) {
        return Fix.dump(value.cast(this), false);
    }

    @Override public IValue mul(final IValue lhs, final IValue rhs) {
        return new Value<>(this, Fix.mul(lhs.cast(this), rhs.cast(this)));
    }

    @Override public IValue sub(final IValue lhs, final IValue rhs) {
        return new Value<>(this, Fix.sub(lhs.cast(this), rhs.cast(this)));
    }

    @Override public IValue zero() { return CoreLib.FIX_ZERO; }
}
