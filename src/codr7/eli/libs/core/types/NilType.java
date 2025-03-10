package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.VM;
import codr7.eli.libs.core.traits.ComparableTrait;

public final class NilType extends BaseType<Object> {
    public NilType(final String id, final IType...parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "_";
    }

    @Override
    public boolean equalValues(final IValue left, final IValue right) {
        return true;
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return false;
    }
}
