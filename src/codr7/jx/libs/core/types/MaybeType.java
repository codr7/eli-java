package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IType;
import codr7.jx.IValue;
import codr7.jx.VM;

public class MaybeType extends BaseType<IValue> {
    public MaybeType(final String id, final IType...parents) { super(id, parents); }
    @Override public String dump(final VM vm, final IValue value) { return value.dump(vm) + "?"; }
}