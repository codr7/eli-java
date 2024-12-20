package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.Pair;
import codr7.jx.VM;

public class PairType extends BaseType<Pair> {
    public PairType(final String id) { super(id); }
    public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }
}
