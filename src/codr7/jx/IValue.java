package codr7.jx;

public sealed interface IValue permits Value {
    <U> U cast(IDataType<U> type);
    String dump(VM vm);
    IValue copy(VM vm);
    boolean toBit(VM vm);
    String toString(VM vm);
    IType type();
}
