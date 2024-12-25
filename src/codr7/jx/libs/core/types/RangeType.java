package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;
import codr7.jx.Range;

public class RangeType extends BaseType<Range> {
    public RangeType(final String id) { super(id); }

    @Override public String dump(final VM vm, final IValue value) {
        final var v = value.cast(this);
        return "(Range " + v.from() + " " + v.to() + " " + v.stride() + ")";
    }
}
