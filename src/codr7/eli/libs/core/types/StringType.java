package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.iters.StringChars;
import codr7.eli.libs.core.traits.CallTrait;
import codr7.eli.libs.core.traits.CmpTrait;
import codr7.eli.libs.core.traits.SeqTrait;

public class StringType extends BaseType<String> implements CallTrait, CmpTrait, SeqTrait {
    public StringType(final String id, final IType...parents) {
        super(id, parents);
    }

    @Override public void call(final VM vm,
                               final IValue target,
                               final int rArgs,
                               final int arity,
                               final int rResult,
                               final Loc loc) {
        final var t = target.cast(this);

        switch (arity) {
            case 1: {
                final var i = vm.registers.get(rArgs).cast(CoreLib.intType).intValue();
                vm.registers.set(rResult, new Value<>(CoreLib.charType, t.charAt(i)));
                break;
            }
            default:
                throw new EvalError("Invalid arguments", loc);
        }
    }

    @Override public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        return lhs.cast(this).compareTo(rhs.cast(this));
    }

    @Override public String dump(final VM vm, final IValue value) {
        return '"' + value.cast(this) + '"';
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return new StringChars(target.cast(this));
    }

    @Override
    public IValue head(final IValue target) {
        final var t = target.cast(this);
        return t.isEmpty() ? CoreLib.NIL : new Value<>(CoreLib.charType, t.charAt(0));
    }

    @Override public int len(final IValue target) { return target.cast(this).length(); }

    @Override
    public IValue tail(final IValue target) {
        final var t = target.cast(this);

        return t.isEmpty()
                ? CoreLib.NIL
                : new Value<>(CoreLib.charType, t.charAt(t.length()-1));
    }

    @Override public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isEmpty();
    }

    @Override public String toString(final VM vm, final IValue value) {
        return value.cast(this);
    }
}