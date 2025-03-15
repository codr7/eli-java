package codr7.eli.libs.core;

import codr7.eli.*;

public final class SplatType extends BaseType<Iter> {
    public SplatType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }
}
