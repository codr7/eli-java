package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.libs.core.ComparableTrait;

public sealed interface IValue extends Comparable<IValue> permits Value {
    <U> U cast(IDataType<U> type);

    default void typeCheck(final VM vm, final IType expected, final Loc loc) {
        if (!type().isa(expected)) {
            throw new EvalError("Type check failed, expected " + expected.id() + ": " +
                    dump(vm), loc);
        }
    }

    @Override
    default int compareTo(final IValue right) {
        if (type() instanceof ComparableTrait ct) {
            if (right.type() != type()) {
                throw new RuntimeException("Type mismatch: " + type().id() + '/' + right.type().id());
            }

            return ct.compareValues(this, right);
        } else {
            throw new RuntimeException("Not cmp: " + type().id());
        }

    }

    String dump(VM vm);

    IValue dup(VM vm);

    void emit(VM vm, int rResult, Loc loc);

    boolean eq(IValue other);

    boolean is(IValue other);

    boolean toBit(VM vm);

    String toString(VM vm);

    IType type();

    void unquote(VM vm, int rResult, Loc loc);
}
