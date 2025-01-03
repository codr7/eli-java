package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IValue;
import codr7.jx.VM;

import java.util.List;

public class ListType extends BaseType<List<IValue>> {
    public ListType(final String id) { super(id); }

    @Override public String dump(final VM vm, final IValue value) {
        final var result = new StringBuilder();
        result.append('[');
        final var v = value.cast(this);

        for (var i = 0; i < v.size(); i++) {
            if (i > 0) { result.append(' '); }
            result.append(v.get(i).dump(vm));
        }

        result.append(']');
        return result.toString();
    }

    @Override public boolean eq(IValue left, IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        if (lv.size() != rv.size()) { return false; }

        for (var i = 0; i < lv.size(); i++) {
            if (!lv.get(i).eq(rv.get(i))) { return false; }
        }

        return true;
    }

    @Override public boolean toBit(final VM vm, final IValue value) { return !value.cast(this).isEmpty(); }

    @Override public String toString(final VM vm, final IValue value) {
        final var result = new StringBuilder();
        result.append('[');
        final var v = value.cast(this);

        for (var i = 0; i < v.size(); i++) {
            if (i > 0) { result.append(' '); }
            result.append(v.get(i).toString(vm));
        }

        result.append(']');
        return result.toString();
    }
}