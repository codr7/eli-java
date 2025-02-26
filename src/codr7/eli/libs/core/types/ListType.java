package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.iters.ListItems;
import codr7.eli.libs.core.traits.CallTrait;
import codr7.eli.libs.core.traits.CmpTrait;
import codr7.eli.libs.core.traits.SeqTrait;

import java.util.ArrayList;

public class ListType
        extends BaseType<ArrayList<IValue>>
        implements CallTrait, CmpTrait, SeqTrait {
    public ListType(final String id, IType...parents) { super(id, parents); }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        final var t = target.cast(this);

        switch (arity) {
            case 1: {
                final var i = vm.registers.get(rArgs).cast(CoreLib.intType).intValue();
                vm.registers.set(rResult, t.get(i));
                break;
            }

            case 2: {
                final var i = vm.registers.get(rArgs).cast(CoreLib.intType).intValue();
                final var v = vm.registers.get(rArgs+1);
                t.set(i, v);
                break;
            }

            default:
                throw new EvalError("Invalid args", loc);
        }
    }

    @Override
    public int cmp(final IValue lhs, final IValue rhs) {
        final var ll = lhs.cast(this);
        final var rl = rhs.cast(this);

        for (var i = 0; i < Math.min(ll.size(), rl.size()); i++) {
            final var lv = ll.get(i);

            if (lv.type() instanceof CmpTrait ct) {
                final var rv = rl.get(i);
                final var r = ct.cmp(lv, rv);
                if (r != 0) { return r; }
            } else {
                throw new RuntimeException("Expected cmp: " + lv.type().toString());
            }
        }

        if (ll.size() != rl.size()) { return Integer.compare(ll.size(), rl.size()); }
        return 0;
    }

    @Override
    public String dump(final VM vm, final IValue value) {
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

    @Override
    public boolean eq(IValue left, IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        if (lv.size() != rv.size()) { return false; }

        for (var i = 0; i < lv.size(); i++) {
            if (!lv.get(i).eq(rv.get(i))) { return false; }
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
    public int len(final IValue target) { return target.cast(this).size(); }

    @Override
    public IValue tail(IValue target) {
        final var t = target.cast(this);

        return t.isEmpty()
                ? CoreLib.NIL
                : new Value<>(this, new ArrayList<>(t.subList(1, t.size())));
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