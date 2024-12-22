package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.libs.Core;
import codr7.jx.ops.AddItem;
import codr7.jx.ops.CreateList;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class ListForm extends BaseForm {
    private final IForm[] items;

    public ListForm(IForm[] items, final Location location) {
        super(location);
        this.items = items;
    }

    public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            vm.emit(CreateList.make(rResult, location()));
            final var rItem = vm.alloc(1);

            for (final var it: items) {
                it.emit(vm, rItem);
                vm.emit(AddItem.make(rResult, rItem, it.location()));
            }
        } else {
            v.emit(vm, rResult, location());
        }
    }

    public String toString(VM vm) {
        final var result = new StringBuilder();
        result.append('[');

        for (var i = 0; i < items.length; i++) {
            if (i > 0) { result.append(' '); }
            result.append(items[i].toString(vm));
        }

        result.append(']');
        return result.toString();
    }

    public Stream<IValue> itemValues(VM vm) {  return Arrays.stream(items).map(it -> it.value(vm)); }

    public IValue value(VM vm) {
        final var vs = Arrays.stream(items).map(it -> it.value(vm));
        if (itemValues(vm).anyMatch(Objects::isNull)) { return null; }
        return new Value<>(Core.listType, itemValues(vm).toList());
    }
}