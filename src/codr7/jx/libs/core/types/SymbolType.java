package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;
import codr7.jx.libs.core.traits.CmpTrait;

public class SymbolType extends BaseType<String> implements CmpTrait {
    public SymbolType(final String id) {
        super(id);
    }

    @Override public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override public String dump(final VM vm, final IValue value) { return "'" + value.cast(this); }
    @Override public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).equals("_");
    }
    @Override public String toString(final VM vm, final IValue value) {
        return value.cast(this);
    }
}