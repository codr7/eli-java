package codr7.eli.libs.core.traits;

import codr7.eli.IForm;
import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.ops.CallValue;

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

    default void emitCall(VM vm, IValue target, IForm[] body, int rResult, Loc loc) {
        final var arity = body.length - 1;
        final var rArgs = vm.alloc(arity);
        for (var i = 0; i < arity; i++) { body[i+1].emit(vm, rArgs + i); }
        vm.emit(new CallValue(target, rArgs, arity, rResult, loc));
    }
}
