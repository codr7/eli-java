package codr7.jx;

import java.util.Deque;
import codr7.jx.ops.Call;
import codr7.jx.ops.Goto;
import codr7.jx.ops.Nop;
import codr7.jx.ops.Stop;

public interface IForm {
    void emit(VM vm, int rResult);

    default void emitCall(final VM vm, final IForm[] body, final int rResult) {
        final var rTarget = vm.alloc(1);
        emit(vm, rTarget);
        final var arity = body.length;
        final var rParams = vm.alloc(arity);
        vm.emit(Call.make(rTarget, rParams, arity, rResult, location()));
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
