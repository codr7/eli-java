package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.core.iters.ListItems;
import codr7.jx.libs.core.traits.CallTrait;
import codr7.jx.libs.core.traits.SeqTrait;

import java.util.ArrayList;

public class ListType extends BaseType<ArrayList<IValue>> implements CallTrait, SeqTrait {
    public ListType(final String id) { super(id); }

    @Override public void call(final VM vm,
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
            default:
                throw new EvalError("Invalid arguments", loc);
        }
    }

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

    @Override public Iter iter(VM vm, IValue target, Loc loc) { return new ListItems(target.cast(this)); }

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