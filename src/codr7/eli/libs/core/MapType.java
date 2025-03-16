package codr7.eli.libs.core;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapType
        extends BaseType<TreeMap<IValue, IValue>>
        implements CallableTrait, ComparableTrait, CountableTrait, IterableTrait, SequentialTrait {
    public MapType(final String id, IType... parents) {
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
                final var k = args[0];
                final var v = t.get(k);
                vm.registers.set(rResult, (v == null) ? CoreLib.NIL : v);
                break;
            }

            case 2: {
                final var k = args[0];
                final var v = args[1];
                t.put(k, v);
                break;
            }

            default:
                throw new EvalError("Invalid args", loc);
        }
    }

    @Override
    public int compareValues(final IValue lhs, final IValue rhs) {
        final var lm = lhs.cast(this);
        final var rm = rhs.cast(this);
        final var lks = lm.navigableKeySet().iterator();
        final var rks = rm.navigableKeySet().iterator();

        while (lks.hasNext() && rks.hasNext()) {
            final var lk = lks.next();
            final var rk = rks.next();

            if (lk.type() != rk.type()) {
                throw new RuntimeException("Type mismatch: " + lk.type().id() + '/' + rk.type().id());
            }

            if (lk.type() instanceof ComparableTrait ct) {
                final var r = ct.compareValues(lk, rk);
                if (r != 0) {
                    return r;
                }
            } else {
                throw new RuntimeException("Expected cmp: " + lk.type().toString());
            }
        }

        if (lm.size() != rm.size()) {
            return Integer.compare(lm.size(), rm.size());
        }
        return 0;
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return '{' +
                value.cast(this).entrySet().stream()
                        .map(e -> e.getKey().dump(vm) + ':' + e.getValue().dump(vm))
                        .collect(Collectors.joining(" ")) +
                '}';
    }

    @Override
    public IValue dup(VM vm, IValue source) {
        return new Value<>(this, new TreeMap<>(source.cast(this)));
    }

    @Override
    public boolean equalValues(IValue left, IValue right) {
        final var lm = left.cast(this);
        final var rm = right.cast(this);
        if (lm.size() != rm.size()) {
            return false;
        }
        final var les = lm.entrySet().iterator();
        final var res = rm.entrySet().iterator();

        while (les.hasNext()) {
            final var le = les.next();
            final var re = res.next();

            if (!le.getKey().eq(re.getKey()) || !le.getValue().eq(re.getValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iter iter(final VM vm, final IValue target) {
        final Stream<IValue> s = target.cast(this)
                .entrySet()
                .stream()
                .map(e -> new Value<>(CoreLib.Pair, new Pair(e.getKey(), e.getValue())));

        return new StreamItems(s);
    }

    @Override
    public IValue head(final IValue target) {
        final var t = target.cast(this);

        if (t.isEmpty()) {
            return CoreLib.NIL;
        }

        final var e = t.firstEntry();
        return new Value<>(CoreLib.Pair, new Pair(e.getKey(), e.getValue()));
    }

    @Override
    public int count(final IValue target) {
        return target.cast(this).size();
    }

    @Override
    public IValue tail(IValue target) {
        final var t = target.cast(this);

        if (t.isEmpty()) {
            return CoreLib.NIL;
        }

        return new Value<>(this, new TreeMap<>(t.tailMap(t.firstKey(), false)));
    }

    @Override
    public boolean toBit(final VM vm, final IValue value) {
        return !value.cast(this).isEmpty();
    }

    @Override
    public String toString(final VM vm, final IValue value) {
        return '{' +
                value.cast(this).entrySet().stream()
                        .map(e -> e.getKey().toString(vm) + ':' + e.getValue().toString(vm))
                        .collect(Collectors.joining(" ")) +
                '}';
    }
}