package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;

public class AnyType extends BaseType<Void> {
    public AnyType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return "?"; }
}