package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.ListAdd;

import java.util.ArrayList;
import java.util.Objects;

public class ListForm extends BaseForm {
    public final IForm[] items;

    public ListForm(IForm[] items, final Loc loc) {
        super(loc);
        this.items = items;
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            new Value<>(CoreLib.List, new ArrayList<>()).emit(vm, rResult, loc());
            final var rItem = vm.alloc(1);

            for (final var it : items) {
                it.emit(vm, rItem);
                vm.emit(new ListAdd(rResult, rItem, it.loc()));
            }
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof ListForm f) {
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
        result.append('[');

        for (var i = 0; i < items.length; i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(items[i].dump(vm));
        }

        result.append(']');
        return result.toString();
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        final var result = new ArrayList<IValue>();
        for (final var it : items) {
            result.add(it.quote(vm, loc));
        }
        return new Value<>(CoreLib.List, result);
    }

    @Override
    public IValue rawValue(final VM vm) {
        final var out = Form.rawValues(vm, items);
        return (out == null) ? null : new Value<>(CoreLib.List, new ArrayList<>(out.toList()));
    }

    @Override
    public IValue value(VM vm) {
        final var out = Form.values(vm, items);
        return (out == null) ? null : new Value<>(CoreLib.List, new ArrayList<>(out.toList()));
    }
}