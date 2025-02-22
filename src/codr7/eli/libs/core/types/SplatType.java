package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.SeqTrait;

public class SplatType extends BaseType<Iter> implements SeqTrait {
    public SplatType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }
    @Override public Iter iter(VM vm, IValue target) { return target.cast(this); }
}
