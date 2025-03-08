package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.CallableTrait;

public final class JMethodType extends BaseType<JMethod> implements CallableTrait {
    public JMethodType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        target.cast(this).call(vm, args, rResult, loc);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "(JMethod " + value.cast(this).id() + ")";
    }
}
