package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;
import codr7.jx.libs.core.traits.CmpTrait;

public class NilType extends BaseType<Object> implements CmpTrait {
    public NilType(final String id) { super(id); }

    @Override public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        return lhs.type().id().compareTo(rhs.type().id());
    }

    @Override public String dump(final VM vm, final IValue value) { return "_"; }
    @Override public boolean eq(IValue left, IValue right) { return true; }
    @Override public boolean toBit(final VM vm, final IValue value) { return false; }
}
