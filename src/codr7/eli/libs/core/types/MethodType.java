package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.CallTrait;

public final class MethodType extends BaseType<Method> implements CallTrait {
    public MethodType(final String id) {
        super(id);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        target.cast(this).call(vm, rArgs, arity, rResult, eval, loc);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }
}