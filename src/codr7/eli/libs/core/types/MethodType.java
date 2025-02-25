package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
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
                     final Loc loc) {
        throw new EvalError("Indirect call", loc);
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Loc loc) {
        final var m = target.cast(this);

        vm.doLib(null, () -> {
            var i = 1;
            var rArg = m.rArgs();

            for (final var a: m.args()) {
                i = a.bind(vm, body, i, rArg, loc);
                rArg++;
            }

            m.emitBody(vm, m.rArgs(), rResult, loc);
        });
    }
}