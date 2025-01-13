package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.libs.CoreLib;
import codr7.jx.ops.AddItem;
import codr7.jx.ops.CreateList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class ListForm extends BaseForm {
    public final IForm[] items;

    public ListForm(IForm[] items, final Loc loc) {
        super(loc);
        this.items = items;
    }

    @Override public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            vm.emit(new CreateList(rResult, loc()));
            final var rItem = vm.alloc(1);

            for (final var it: items) {
                it.emit(vm, rItem);
                vm.emit(new AddItem(rResult, rItem, it.loc()));
            }
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override public boolean eq(final IForm other) {
        if (other instanceof ListForm f) {
            if (f.items.length != items.length) { return false; }

            for (var i = 0; i < items.length; i++) {
                if (!f.items[i].eq(items[i])) { return false; }
            }

            return true;
        }

        return false;
    }

    @Override public String dump(VM vm) {
        final var result = new StringBuilder();
        result.append('[');

        for (var i = 0; i < items.length; i++) {
            if (i > 0) { result.append(' '); }
            result.append(items[i].dump(vm));
        }

        result.append(']');
        return result.toString();
    }

    public Stream<IValue> itemValues(VM vm) {  return Arrays.stream(items).map(it -> it.value(vm)); }

    @Override public IValue quote(final VM vm, final Loc loc) {
        final var result = new ArrayList<IValue>();
        for (final var it: items) { result.add(it.quote(vm, loc)); }
        return new Value<>(CoreLib.listType, result);
    }

    @Override public IValue value(VM vm) {
        final var vs = Arrays.stream(items).map(it -> it.value(vm));
        if (itemValues(vm).anyMatch(Objects::isNull)) { return null; }
        return new Value<>(CoreLib.listType, new ArrayList<>(itemValues(vm).toList()));
    }
}