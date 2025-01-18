package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.core.traits.CallTrait;

public class StringType extends BaseType<String> implements CallTrait {
    public StringType(final String id) {
        super(id);
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

    @Override public String dump(final VM vm, final IValue value) {
        return '"' + value.cast(this) + '"';
    }

    @Override public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isEmpty();
    }

    @Override public String toString(final VM vm, final IValue value) {
        return value.cast(this);
    }
}