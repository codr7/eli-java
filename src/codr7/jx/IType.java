package codr7.jx;

import codr7.jx.ops.Put;

public interface IType {
    void addParentType(IType type, int weight);
    void addParentTypes(IType childType);
    default IValue dup(VM vm, IValue source) { return source; }
    String dump(VM vm, IValue value);
    default void emit(VM vm, IValue value, int rResult, Loc loc) {
        vm.emit(Put.make(rResult, value, loc));
    }
    boolean equals(IValue left, IValue right);
    String id();
    boolean is(IValue left, IValue right);
    default boolean toBit(VM vm, IValue value) { return true; }
    default String toString(VM vm, IValue value) { return dump(vm, value); }
}
