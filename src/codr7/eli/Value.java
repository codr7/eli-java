package codr7.eli;

import codr7.eli.libs.CoreLib;

import java.util.List;

public record Value<T>(IDataType<T> type, T data) implements IValue {
    public static void expand(final VM vm,
                              final IValue value,
                              final List<IValue> out,
                              final Loc loc) {
        if (value.type() == CoreLib.Splat) {
            final var rValue = vm.alloc(1);
            final var it = value.cast(CoreLib.Splat);

            while (it.next(vm, rValue, loc)) {
                expand(vm, vm.registers.get(rValue), out, loc);
            }
        } else {
            out.add(value);
        }
    }

    public <U> U cast(IDataType<U> type) {
        return (U) data;
    }

    public IValue dup(final VM vm) {
        return type.dup(vm, this);
    }

    public String dump(final VM vm) {
        return type.dump(vm, this);
    }

    public boolean eq(final IValue other) {
        return other instanceof IValue v && v.type() == type && type.eq(this, other);
    }

    public void emit(final VM vm, final int rResult, final Loc loc) {
        type.emit(vm, this, rResult, loc);
    }

    @Override
    public boolean is(final IValue other) {
        return type == other.type() && type.is(this, other);
    }

    public boolean toBit(final VM vm) {
        return type.toBit(vm, this);
    }

    public String toString(final VM vm) {
        return type.toString(vm, this);
    }

    public void unquote(VM vm, int rResult, Loc loc) {
        type.unquote(vm, this, rResult, loc);
    }
}