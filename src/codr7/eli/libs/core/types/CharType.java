package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IValue;
import codr7.eli.VM;

public class CharType extends BaseType<Character> {
    public CharType(final String id) { super(id); }

    @Override public String dump(final VM vm, final IValue value) { return "\\" + value.cast(this); }

    @Override public boolean toBit(final VM vm, final IValue value) { return value.cast(this) != 0; }
}
