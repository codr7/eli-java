package codr7.eli.libs.core;

import codr7.eli.BaseType;
import codr7.eli.IType;

public final class MetaType extends BaseType<IType> {
    public MetaType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }
}
