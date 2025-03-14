package codr7.eli.libs.core.types;

import codr7.eli.*;

public final class RangeType extends BaseType<Range> {
    public RangeType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        final var v = value.cast(this);
        return "(Range " + v.from() + " " + v.to() + " " + v.stride() + ")";
    }
}
