package codr7.jx;

import codr7.jx.ops.Put;

public interface IType {
    default IValue copy(VM vm, IValue source) { return source; }
    String dump(VM vm, IValue value);

    default void emit(VM vm, IValue value, int rResult, Location location) {
        vm.emit(Put.make(rResult, value, location));
    }

    String id();
    default boolean toBit(VM vm, IValue value) { return true; }
    default String toString(VM vm, IValue value) { return dump(vm, value); }
}
