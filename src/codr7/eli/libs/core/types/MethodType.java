package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.Callable;

public final class MethodType extends BaseType<Method> implements Callable {
    public MethodType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        target.cast(this).call(vm, args, rResult, eval, loc);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }
}