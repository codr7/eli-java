package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.libs.core.traits.CmpTrait;

public class PairType extends BaseType<Pair> implements CmpTrait {
    public PairType(final String id) { super(id); }

    @Override public int cmp(final VM vm, final IValue lhs, final IValue rhs, final Loc loc) {
        final var lp = lhs.cast(this);
        final var rp = rhs.cast(this);

        if (lhs.type() instanceof CmpTrait ct) {
            final var lr = ct.cmp(vm, lp.left(), rp.left(), loc);
            if (lr != 0) { return lr; }
            final var rr = ct.cmp(vm, lp.right(), rp.right(), loc);
            if (rr != 0) { return rr; }
        }

        return 0;
    }

    @Override public String dump(final VM vm, final IValue value) { return value.cast(this).dump(vm); }

    @Override public boolean eq(IValue left, IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        return lv.left().equals(rv.left()) && lv.right().equals(rv.right());
    }
}
