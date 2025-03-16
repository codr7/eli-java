package codr7.eli.libs.core;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;

public class ListType
        extends BaseType<ArrayList<IValue>>
        implements CallableTrait, ComparableTrait, CountableTrait, SequentialTrait {
    public ListType(final String id, IType... parents) {
        super(id, parents);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        final var t = target.cast(this);

        switch (args.length) {
            case 1: {
                final var i = args[0].cast(CoreLib.Int).intValue();
                vm.registers.set(rResult, t.get(i));
                break;
            }
            case 2: {
                final var i = args[0].cast(CoreLib.Int).intValue();
                final var v = args[1];
                t.set(i, v);
                break;
            }
            default:
                throw new EvalError("Invalid args", loc);
        }
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        final var ll = lhs.cast(this);
        final var rl = rhs.cast(this);

        for (var i = 0; i < Math.min(ll.size(), rl.size()); i++) {
            final var lv = ll.get(i);

            if (lv.type() instanceof ComparableTrait ct) {
                final var rv = rl.get(i);
                final var r = ct.compareValues(lv, rv);
                if (r != 0) {
                    return r;
                }
            } else {
                throw new RuntimeException("Expected cmp: " + lv.type().toString());
            }
        }

        if (ll.size() != rl.size()) {
            return Integer.compare(ll.size(), rl.size());
        }
        return 0;
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        final var result = new StringBuilder();
        result.append('[');
        final var v = value.cast(this);

        for (var i = 0; i < v.size(); i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(v.get(i).dump(vm));
        }

        result.append(']');
        return result.toString();
    }

    @Override
    public boolean equalValues(IValue left, IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        if (lv.size() != rv.size()) {
            return false;
        }

        for (var i = 0; i < lv.size(); i++) {
            if (!lv.get(i).eq(rv.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return new ListItems(target.cast(this));
    }

    @Override
    public IValue head(final IValue target) {
        final var t = target.cast(this);
        return t.isEmpty() ? CoreLib.NIL : t.getFirst();
    }

    @Override
    public int count(final IValue target) {
        return target.cast(this).size();
    }

    @Override
    public IValue dup(VM vm, IValue source) {
        return new Value<>(this, new ArrayList<>(source.cast(this)));
    }

    @Override
    public IValue tail(IValue target) {
        final var t = target.cast(this);

        return t.isEmpty()
                ? CoreLib.NIL
                : new Value<>(this, new ArrayList<>(t.subList(1, t.size())));
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isEmpty();
    }

    @Override
    public String toString(final VM vm, final IValue value) {
        final var result = new StringBuilder();
        final var v = value.cast(this);

        for (var i = 0; i < v.size(); i++) {
            if (i > 0) {
                result.append(' ');
            }

            result.append(v.get(i).toString(vm));
        }

        return result.toString();
    }
}