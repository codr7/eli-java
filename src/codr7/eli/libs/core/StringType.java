package codr7.eli.libs.core;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

public class StringType extends BaseType<String>
        implements CallableTrait, ComparableTrait, CountableTrait, IterableTrait, SequentialTrait {
    public StringType(final String id, final IType... parents) {
        super(id, parents);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        final var t = target.cast(this);

        switch (args.length) {
            case 1: {
                final var i = args[0].cast(CoreLib.Int).intValue();
                vm.registers.set(rResult, new Value<>(CoreLib.Char, t.charAt(i)));
                break;
            }
            default:
                throw new EvalError("Invalid arguments", loc);
        }
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return '"' + value.cast(this) + '"';
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return new StringChars(target.cast(this));
    }

    @Override
    public IValue head(final IValue target) {
        final var t = target.cast(this);
        return t.isEmpty() ? CoreLib.NIL : new Value<>(CoreLib.Char, t.charAt(0));
    }

    @Override
    public int count(final IValue target) {
        return target.cast(this).length();
    }

    @Override
    public IValue tail(final IValue target) {
        final var t = target.cast(this);

        return t.isEmpty()
                ? CoreLib.NIL
                : new Value<>(CoreLib.Char, t.charAt(t.length() - 1));
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isEmpty();
    }

    @Override
    public String toString(final VM vm, final IValue value) {
        return value.cast(this);
    }
}