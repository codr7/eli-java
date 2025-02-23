package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;
import codr7.eli.IValue;
import codr7.eli.VM;

public final class MaybeType extends BaseType<IValue> {
    public MaybeType(final String id, final IType...parents) { super(id, parents); }

    @Override
    public String dump(final VM vm, final IValue value) { return value.dump(vm) + "?"; }
}