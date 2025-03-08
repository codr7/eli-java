package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IValue;
import codr7.eli.VM;

import java.time.Duration;

public final class TimeType extends BaseType<Duration> {
    public TimeType(final String id) {
        super(id);
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isZero();
    }
}
