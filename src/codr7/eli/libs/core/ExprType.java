package codr7.eli.libs.core;

import codr7.eli.*;

public final class ExprType extends BaseType<IForm> {
    public ExprType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public boolean equalValues(final IValue left, final IValue right) {
        return left.cast(this).eq(right.cast(this));
    }

    @Override
    public void unquote(final VM vm, final IValue value, final int rResult, final Loc loc) {
        value.cast(this).unquote(vm, rResult, loc);
    }
}
