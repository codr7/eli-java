package codr7.jx;

public interface IType {
    default IValue copy(VM vm, IValue source) { return source; }
    String dump(VM vm, IValue value);
    String id();
    default boolean toBit(VM vm, IValue value) { return true; }
    default String toString(VM vm, IValue value) { return dump(vm, value); }
}
