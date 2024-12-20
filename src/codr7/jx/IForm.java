package codr7.jx;

import java.util.Deque;
import codr7.jx.ops.Call;

public interface IForm {
    void emit(VM vm, int rResult);

    default void emitCall(final VM vm, final IForm[] body, final int rResult) {
        final var rTarget = vm.alloc(1);
        emit(vm, rTarget);
        final var arity = body.length;
        final var rParams = vm.alloc(arity);
        vm.emit(Call.make(rTarget, rParams, arity, rResult, location()));
    }

    default boolean isNil() { return false; }
    Location location();
    String toString(VM vm);
    IValue value(VM vm);
}
