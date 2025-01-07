package codr7.jx.libs.core.types;

import codr7.jx.IForm;
import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;

public interface CallTrait {
    void call(VM vm, IValue target, int rArgs, int arity, int rResult, Loc loc);

    default void call(final VM vm,
                      final IValue target,
                      final IValue[] args,
                      final int rResult,
                      final Loc loc) {
        final var rArgs = vm.alloc(args.length);
        for (var i = 0; i < args.length; i++) { vm.registers.set(rArgs + i, args[i]); }
        call(vm, target, rArgs, args.length, rResult, loc);
    }

    void emitCall(VM vm, IValue target, IForm[] body, int rResult, Loc loc);
}
