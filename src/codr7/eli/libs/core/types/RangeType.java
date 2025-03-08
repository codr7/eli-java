package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IValue;
import codr7.eli.Range;
import codr7.eli.VM;

public final class RangeType extends BaseType<Range> {
    public RangeType(final String id) {
        super(id);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        final var v = value.cast(this);
        return "(Range " + v.from() + " " + v.to() + " " + v.stride() + ")";
    }
}
