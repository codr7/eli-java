package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.core.traits.CallTrait;

public class MethodType extends BaseType<Method> implements CallTrait {
    public MethodType(final String id) {
        super(id);
    }

    @Override public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        target.cast(this).call(vm, rArgs, arity, rResult, loc);
    }

    @Override public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override public void emitCall(final VM vm,
                                    final IValue target,
                                    final IForm[] body,
                                    final int rResult,
                                    final Loc loc) {
        final var m = target.cast(this);
        final var arity = body.length - 1;

        vm.doLib(null, () -> {
            for (var i = 0; i < arity; i++) {
                final var ma = m.args()[i];

                if (ma.id().charAt(0) == '\'') {
                    vm.currentLib.bind(ma.id().substring(1), new Value<>(CoreLib.exprType, body[i + 1]));
                } else {
                    body[i + 1].emit(vm, m.rArgs() + i);
                }
            }

            m.emitBody(vm, m.rArgs(), rResult, loc);
        });
    }
}