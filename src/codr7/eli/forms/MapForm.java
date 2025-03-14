package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.MapAdd;

import java.util.TreeMap;

public class MapForm extends BaseForm {
    public final PairForm[] items;

    public MapForm(final PairForm[] items, final Loc loc) {
        super(loc);
        this.items = items;
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            new Value<>(CoreLib.Map, new TreeMap<>()).emit(vm, rResult, loc());
            final var rKey = vm.alloc(1);
            final var rValue = vm.alloc(1);

            for (final var it : items) {
                it.left.emit(vm, rKey);
                it.right.emit(vm, rValue);
                vm.emit(new MapAdd(rResult, rKey, rValue, it.loc()));
            }
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof MapForm f) {
            if (f.items.length != items.length) {
                return false;
            }

            for (var i = 0; i < items.length; i++) {
                if (!f.items[i].eq(items[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String dump(VM vm) {
        final var result = new StringBuilder();
        result.append('{');

        for (var i = 0; i < items.length; i++) {
            if (i > 0) {
                result.append(' ');
            }

            result.append(items[i].dump(vm));
        }

        result.append('}');
        return result.toString();
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        final var result = new TreeMap<IValue, IValue>();

        for (final var it : items) {
            result.put(it.left.quote(vm, loc), it.right.quote(vm, loc));
        }

        return new Value<>(CoreLib.Map, result);
    }

    @Override
    public IValue rawValue(VM vm) {
        final var vs = new TreeMap<IValue, IValue>();

        for (final var it : items) {
            final var k = it.left.rawValue(vm);

            if (k == null) {
                return null;
            }

            final var v = it.right.rawValue(vm);

            if (v == null) {
                return null;
            }

            vs.put(k, v);
        }

        return new Value<>(CoreLib.Map, vs);
    }

    @Override
    public IValue value(VM vm) {
        final var vs = new TreeMap<IValue, IValue>();

        for (final var it : items) {
            final var k = it.left.value(vm);

            if (k == null) {
                return null;
            }

            final var v = it.right.rawValue(vm);

            if (v == null) {
                return null;
            }

            vs.put(k, v);
        }

        return new Value<>(CoreLib.Map, vs);
    }
}