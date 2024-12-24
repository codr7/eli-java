package codr7.jx;

import codr7.jx.errors.EmitError;
import codr7.jx.libs.Core;
import codr7.jx.ops.CallRegister;
import codr7.jx.ops.CallValue;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;

public interface IForm {
    void emit(VM vm, int rResult);

    default void emitCall(final VM vm, final IForm[] body, final int rResult) {
        final var arity = body.length;
        final var rParams = vm.alloc(arity);
        final var t = value(vm);

        if (t == null) {
            final var rTarget = vm.alloc(1);
            emit(vm, rTarget);
            vm.emit(CallRegister.make(rTarget, rParams, arity, rResult, loc()));
        } else {
            vm.emit(CallValue.make(t, rParams, arity, rResult, loc()));
        }
    }

    default IValue eval(final VM vm) {
        final var v = value(vm);
        if (v != null) { return v; }
        final var rResult = vm.alloc(1);
        final var skipPc = vm.emit(Nop.make(loc()));
        final var startPc = vm.emitPc();
        emit(vm, rResult);
        vm.ops.set(skipPc, Goto.make(vm.emitPc(), loc()));
        vm.eval(startPc);
        return vm.registers.get(rResult);
    }

    default IType getType(final VM vm, final Loc loc) {
        final var v = value(vm);
        if (v == null) { throw new EmitError("Expected type: " + dump(vm), loc); }
        final var t = v.cast(Core.metaType);
        if (t == null) { throw new EmitError("Expected type: " + dump(vm), loc); }
        return t;
    }

    default boolean isNil() { return false; }
    Loc loc();
    String dump(VM vm);
    IValue value(VM vm);
}
