package codr7.eli.libs.core.types;

import codr7.eli.*;
import codr7.eli.libs.core.iters.PairItems;
import codr7.eli.libs.core.traits.ComparableTrait;
import codr7.eli.libs.core.traits.CountableTrait;
import codr7.eli.libs.core.traits.SequentialTrait;
import codr7.eli.libs.core.traits.IterableTrait;

public class PairType extends BaseType<Pair>
        implements ComparableTrait, CountableTrait, IterableTrait, SequentialTrait {
    public PairType(final String id, final IType... parents) {
        super(id, parents);
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        final var lp = lhs.cast(this);
        final var rp = rhs.cast(this);

        if (lhs.type() instanceof ComparableTrait ct) {
            final var lr = ct.compareValues(lp.left(), rp.left());

            if (lr != 0) {
                return lr;
            }

            return ct.compareValues(lp.right(), rp.right());
        }

        throw new RuntimeException("Not comparable");
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return value.cast(this).dump(vm);
    }

    @Override
    public boolean equalValues(final IValue left, final IValue right) {
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
    public int count(final IValue target) {
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
