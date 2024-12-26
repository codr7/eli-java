package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;

import java.time.Duration;

public class TimeType extends BaseType<Duration> {
    public TimeType(final String id) { super(id); }

    @Override public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isZero();
    }
}
