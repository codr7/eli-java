package codr7.eli.libs.core;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.VM;

public final class CharType extends BaseType<Character> {
    public CharType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "\\" + value.cast(this);
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return value.cast(this) != 0;
    }

    @Override
    public String toString(final VM vm, final IValue value) {
        return Character.toString(value.cast(this));
    }
}
