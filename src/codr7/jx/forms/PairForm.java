package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.libs.Core;
import codr7.jx.ops.Zip;

import java.util.ArrayList;

public final class PairForm extends BaseForm {
    public final IForm left, right;

    public PairForm(final IForm left, final IForm right, final Loc loc) {
        super(loc);
        this.left = left;
        this.right = right;
    }

    @Override public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            final var rLeft = vm.alloc(1);
            left.emit(vm, rLeft);
            right.emit(vm, rResult);
            vm.emit(new Zip(rLeft, rResult, rResult, loc()));
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override public boolean eq(final IForm other) {
        if (other instanceof PairForm f) {
            return left.eq(f.left) && right.eq(f.right);
        }

        return false;
    }

    @Override public String dump(final VM vm) {
        return left.toString() + ":" + right.toString();
    }

    @Override public IValue quote(final VM vm, final Loc loc) {
        return new Value<>(Core.pairType, new Pair(left.quote(vm, loc), right.value(vm)));
    }

    @Override public IValue value(final VM vm) {
        final var lv = left.value(vm);

        if (lv != null) {
            final var rv = right.value(vm);
            if (rv != null) { return new Value<>(Core.pairType, new Pair(lv, rv)); }
        }

        return null;
    }
}