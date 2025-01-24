package codr7.jx.libs.core.types;

import codr7.jx.*;

public class ExprType extends BaseType<IForm> {
    public ExprType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }

    @Override public boolean eq(IValue left, IValue right) {
        return left.cast(this).eq(right.cast(this));
    }

    @Override public void unquote(VM vm, IValue value, int rResult, Loc loc) {
        value.cast(this).unquote(vm, rResult, loc);
    }
}
