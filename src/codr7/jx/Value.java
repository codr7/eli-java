package codr7.jx;

public record Value<T>(IDataType<T> type, T data) implements IValue {
    public <U> U cast(IDataType<U> type) { return (U)data; }
    public IValue dup(final VM vm) { return type.dup(vm, this); }
    public String dump(final VM vm) { return type.dump(vm, this); }

    public boolean eq(final IValue other) {
        return other instanceof IValue v && v.type() == type && type.eq(this, (Value<T>)other);
    }

    public void emit(final VM vm, final int rResult, final Loc loc) {
        type.emit(vm, this, rResult, loc);
    }

    @Override public boolean is(final IValue other) {
        return type == other.type() && type.is(this, other);
    }

    public boolean toBit(final VM vm) { return type.toBit(vm, this); }
    public String toString(final VM vm) { return type.toString(vm, this); }

    public void unquote(VM vm, int rResult, Loc loc) { type.unquote(vm, this, rResult, loc); }
}