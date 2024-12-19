package codr7.jx;

public abstract class BaseType<T> implements IDataType<T> {
    public String id;
    public BaseType(final String id) { this.id = id; }
    @Override public String dump(VM vm, IValue value) { return value.cast(this).toString(); }
    @Override public String id() { return id; }
}

