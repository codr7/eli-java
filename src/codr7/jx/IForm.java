package codr7.jx;

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
            vm.emit(CallRegister.make(rTarget, rParams, arity, rResult, location()));
        } else {
            vm.emit(CallValue.make(t, rParams, arity, rResult, location()));
        }
    }

    default IValue eval(final VM vm) {
        final var v = value(vm);
        if (v != null) { return v; }
        final var rResult = vm.alloc(1);
        final var skipPc = vm.emit(Nop.make(location()));
        final var startPc = vm.emitPc();
        emit(vm, rResult);
        vm.ops.set(skipPc, Goto.make(vm.emitPc(), location()));
        vm.eval(startPc);
        return vm.registers.get(rResult);
    }

    default boolean isNil() { return false; }
    Location location();
    String toString(VM vm);
    IValue value(VM vm);
}
