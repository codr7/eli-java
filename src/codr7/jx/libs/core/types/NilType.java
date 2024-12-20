package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;

public class NilType extends BaseType<Void> {
    public NilType(final String id) { super(id); }
    public String dump(final VM vm, final IValue value) { return "_"; }
    public boolean toBit(final IValue value) { return false; }
}
