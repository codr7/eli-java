package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.IterTrait;

public class IterType extends BaseType<Iter> implements IterTrait {
    public IterType(final String id) { super(id); }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return target.cast(this);
    }
}
