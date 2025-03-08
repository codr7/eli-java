package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.iters.PairItems;
import codr7.eli.libs.core.traits.CmpTrait;
import codr7.eli.libs.core.traits.LenTrait;
import codr7.eli.libs.core.traits.SeqTrait;

public class PairType extends BaseType<Pair>
        implements CmpTrait, LenTrait, SeqTrait {
    public PairType(final String id, final IType... parents) {
        super(id, parents);
    }

    @Override
    public int cmp(final IValue lhs, final IValue rhs) {
        final var lp = lhs.cast(this);
        final var rp = rhs.cast(this);

        if (lhs.type() instanceof CmpTrait ct) {
            final var lr = ct.cmp(lp.left(), rp.left());
            if (lr != 0) {
                return lr;
            }
            final var rr = ct.cmp(lp.right(), rp.right());
            return rr;
        }

        return 0;
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public boolean eq(final IValue left, final IValue right) {
        final var lv = left.cast(this);
        final var rv = right.cast(this);
        return lv.left().equals(rv.left()) && lv.right().equals(rv.right());
    }

    @Override
    public IValue head(final IValue target) {
        return target.cast(this).left();
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        return new PairItems(target);
    }

    @Override
    public int len(final IValue target) {
        int n = 0;
        var v = target;

        while (v != null) {
            v = (v.type() == this) ? v.cast(this).right() : null;
            n++;
        }

        return n;
    }

    @Override
    public IValue tail(final IValue target) {
        return target.cast(this).right();
    }
}
