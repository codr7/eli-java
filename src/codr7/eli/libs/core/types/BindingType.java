package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.core.traits.CallableTrait;
import codr7.eli.ops.Copy;

public final class BindingType extends BaseType<Binding> implements CallableTrait {
    public BindingType(final String id) {
        super(id);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        final var t = vm.registers.get(target.cast(this).rValue());
        if (t.type() instanceof CallableTrait ct) {
            ct.call(vm, t, args, rResult, eval, loc);
        } else {
            throw new EvalError("Not callable: " + t.dump(vm), loc);
        }
    }

    @Override
    public void emit(final VM vm, final IValue value, final int rResult, final Loc loc) {
        final var rValue = value.cast(this).rValue();
        if (rResult != rValue) {
            vm.emit(new Copy(rValue, rResult, loc));
        }
    }

    @Override
    public void unquote(final VM vm, final IValue value, final int rResult, final Loc loc) {
        emit(vm, value, rResult, loc);
    }
}
