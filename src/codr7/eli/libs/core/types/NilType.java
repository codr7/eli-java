package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.libs.core.traits.CmpTrait;

public final class NilType extends BaseType<Object> implements CmpTrait {
    public NilType(final String id) { super(id); }

    @Override
    public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        return lhs.type().id().compareTo(rhs.type().id());
    }

    @Override
    public String dump(final VM vm, final IValue value) { return "_"; }

    @Override
    public boolean eq(final IValue left, final IValue right) { return true; }

    @Override
    public boolean toBit(final VM vm, final IValue value) { return false; }
}
