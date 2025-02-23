package codr7.eli.libs.core.types;

import codr7.eli.*;

public final class SplatType extends BaseType<Iter> {
    public SplatType(final String id) { super(id); }

    @Override
    public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }
}
