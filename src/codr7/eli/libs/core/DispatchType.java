package codr7.eli.libs.core;

import codr7.eli.*;

public final class DispatchType extends BaseType<Dispatch> implements CallableTrait {
    public DispatchType(final String id, final IType... parentTypes) {
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