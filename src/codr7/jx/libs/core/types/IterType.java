package codr7.jx.libs.core.types;

import codr7.jx.*;

public class IterType extends BaseType<Iter> implements SeqTrait {
    public IterType(final String id) { super(id); }
    @Override public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }
    @Override public Iter iter(VM vm, IValue target, Loc loc) { return target.cast(this); }
}
