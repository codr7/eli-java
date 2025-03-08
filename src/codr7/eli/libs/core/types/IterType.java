package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.Iterable;

public class IterType extends BaseType<Iter> implements Iterable {
    public IterType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return target.cast(this);
    }
}
