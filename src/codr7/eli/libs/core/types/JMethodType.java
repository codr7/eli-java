package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.traits.CallTrait;
import codr7.eli.ops.CallValue;

public final class JMethodType extends BaseType<JMethod> implements CallTrait {
    public JMethodType(final String id) { super(id); }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        target.cast(this).call(vm, rArgs, arity, rResult, loc);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "(JMethod " + value.cast(this).id() + ")";
    }
}
