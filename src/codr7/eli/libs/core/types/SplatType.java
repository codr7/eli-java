package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IValue;
import codr7.eli.Iter;
import codr7.eli.VM;

public final class SplatType extends BaseType<Iter> {
    public SplatType(final String id) {
        super(id);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }
}
