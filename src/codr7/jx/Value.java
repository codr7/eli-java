package codr7.jx;

public record Value<T>(IDataType<T> type, T data) implements IValue {
    public <U> U cast(IDataType<U> type) { return (U)data; }
    public IValue copy(final VM vm) { return type.copy(vm, this); }
    public String dump(final VM vm) { return type.dump(vm, this); }
    public boolean toBit(final VM vm) { return type.toBit(vm, this); }
    public String toString(final VM vm) { return type.toString(vm, this); }
}