package codr7.jx;

import java.util.Deque;
import codr7.jx.ops.Call;

public interface IForm {
    void emit(VM vm, int rResult);

    default void emitCall(VM vm, Deque<IForm> body, int rResult) {
        final var rTarget = vm.getRegisters(1);
        emit(vm, rTarget);

        final var arity = body.size();
        final var rParams = vm.getRegisters(arity);

        for (int i = 0; !body.isEmpty(); i++) {
            body.removeFirst().emit(vm, rParams+i);
        }

        //TODO: Add call op
        vm.emit(Call.make(rTarget, rParams, arity, rResult, location()));
    }

    Location location();
    String toString();
}
