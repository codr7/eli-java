package codr7.eli.libs.core.types;

import codr7.eli.BaseType;
import codr7.eli.IType;

import java.time.LocalDateTime;

public final class TimestampType extends BaseType<LocalDateTime> {
    public TimestampType(final String id, final IType...parentTypes) {
        super(id, parentTypes);
    }
}
