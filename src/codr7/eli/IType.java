package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.ops.Put;

public interface IType {
    void addParentType(IType type, int weight);
    void addParentTypes(IType childType);

    default <U> U cast(final TraitType<U> trait, final Loc loc) {
        if (isa(trait)) { return (U)this; }
        throw new EvalError("Expected " + trait.id() + ": " + id(), loc);
    }

    default IValue dup(VM vm, IValue source) { return source; }
    String dump(VM vm, IValue value);
    default void emit(VM vm, IValue value, int rResult, Loc loc) {
        vm.emit(new Put(rResult, value, loc));
    }
    boolean eq(IValue left, IValue right);
    String id();
    boolean is(IValue left, IValue right);
    boolean isa(IType type);
    default boolean toBit(VM vm, IValue value) { return true; }
    default String toString(VM vm, IValue value) { return dump(vm, value); }

    default void unquote(VM vm, IValue value, int rResult, Loc loc) {
        value.emit(vm, rResult, loc);
    }
}
