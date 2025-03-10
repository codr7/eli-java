package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.VM;
import codr7.eli.libs.core.traits.ComparableTrait;

public final class SymType extends BaseType<String> implements ComparableTrait {
    public SymType(final String id, final IType...parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "'" + value.cast(this);
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).equals("_");
    }

    @Override
    public String toString(final VM vm, final IValue value) {
        return value.cast(this);
    }
}