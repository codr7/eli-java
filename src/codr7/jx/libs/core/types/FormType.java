package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.IForm;
import codr7.jx.VM;

public class FormType extends BaseType<IForm> {
    public FormType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }

    @Override public boolean eq(IValue left, IValue right) {
        return left.cast(this).eq(right.cast(this));
    }
}
