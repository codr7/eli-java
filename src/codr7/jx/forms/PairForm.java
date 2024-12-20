package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.libs.Core;
import codr7.jx.ops.Zip;

public final class PairForm extends BaseForm {
    public final IForm left, right;

    public PairForm(final IForm left, final IForm right, final Location location) {
        super(location);
        this.left = left;
        this.right = right;
    }

    public void emit(final VM vm, final int rResult) {
        final var rLeft = vm.alloc(1);
        left.emit(vm, rLeft);
        right.emit(vm, rResult);
        vm.emit(Zip.make(rLeft, rResult, rResult, location()));
    }

    public String toString(final VM vm) {
        return left.toString() + ":" + right.toString();
    }

    public IValue value(final VM vm) {
        final var lv = left.value(vm);

        if (lv != null) {
            final var rv = right.value(vm);
            if (rv != null) { return new Value<>(Core.pairType, new Pair(lv, rv)); }
        }

        return null;
    }
}