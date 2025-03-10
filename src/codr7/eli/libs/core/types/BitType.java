package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.VM;
import codr7.eli.libs.core.traits.ComparableTrait;

public final class BitType extends BaseType<Boolean> implements ComparableTrait {
    public BitType(final String id, final IType...parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this) ? "T" : "F";
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return value.cast(this);
    }
}
