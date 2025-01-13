package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;

import java.util.Objects;

public class NilType extends BaseType<Object> {
    public NilType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return "_"; }
    @Override public boolean eq(IValue left, IValue right) { return true; }
    @Override public boolean toBit(final VM vm, final IValue value) { return false; }
}
