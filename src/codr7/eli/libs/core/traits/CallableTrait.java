package codr7.eli.libs.core.traits;

import codr7.eli.*;
import codr7.eli.ops.CallValue;

import java.util.ArrayList;

public interface CallableTrait {

    default void call(final VM vm,
                      final IValue target,
                      final int rArgs,
                      final int arity,
                      final int rResult,
                      final boolean eval,
                      final Loc loc) {
        final var args = new ArrayList<IValue>();

        for (var i = 0; i < arity; i++) {
            final var v = vm.registers.get(rArgs + i);
            Value.expand(vm, v, args, loc);
        }

        call(vm, target, args.toArray(IValue[]::new), rResult, eval, loc);
    }

    void call(final VM vm,
              final IValue target,
              final IValue[] args,
              final int rResult,
              final boolean eval,
              final Loc loc);

    default void emitCall(VM vm, IValue target, IForm[] body, int rResult, Loc loc) {
        final var arity = body.length - 1;
        final var rArgs = vm.alloc(arity);

        for (var i = 0; i < arity; i++) {
            final var f = body[i + 1];
            f.emit(vm, rArgs + i);
        }

        vm.emit(new CallValue(target, rArgs, arity, rResult, loc));
    }
}
