package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;

public class NilType extends BaseType<Void> {
    public NilType(final String id) { super(id); }
    public boolean toBit(final IValue value) { return false; }
}
