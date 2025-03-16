package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.Left;
import codr7.eli.ops.Right;
import codr7.eli.ops.Unzip;
import codr7.eli.ops.Zip;

public final class PairForm extends BaseForm {
    public final IForm left, right;

    public PairForm(final IForm left, final IForm right, final Loc loc) {
        super(loc);
        this.left = left;
        this.right = right;
    }

    @Override
    public void bindValue(final VM vm, final IValue value, final Loc loc) {
        final var p = value.cast(CoreLib.Pair);

        if (left.isNil()) {
            right.bindValue(vm, p.right(), loc);
        } else if (right.isNil()) {
            left.bindValue(vm, p.left(), loc);
        } else {
            left.bindValue(vm, p.left(), loc);
            right.bindValue(vm, p.right(), loc);
        }
    }

    @Override
    public void bindRegister(final VM vm, final int rValue, final IType type, final Loc loc) {
        if (left.isNil()) {
            final var rRight = vm.alloc(1);
            vm.emit(new Right(rValue, rRight, loc));
            right.bindRegister(vm, rRight, type, loc);
        } else if (right.isNil()) {
            final var rLeft = vm.alloc(1);
            vm.emit(new Left(rValue, rLeft, loc));
            left.bindRegister(vm, rLeft, type, loc);
        } else {
            final var rLeft = vm.alloc(1);
            final var rRight = vm.alloc(1);
            vm.emit(new Unzip(rValue, rLeft, rRight, loc));
            left.bindRegister(vm, rLeft, null, loc);
            right.bindRegister(vm, rRight, null, loc);
        }
    }

    @Override
    public String dump(final VM vm) {
        return left.dump(vm) + ":" + right.dump(vm);
    }

    @Override
    public void emit(final VM vm, final int rResult) {
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

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof PairForm f) {
            return left.eq(f.left) && right.eq(f.right);
        }

        return false;
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        return new Value<>(CoreLib.Pair, new Pair(left.quote(vm, loc), right.eval(vm)));
    }

    @Override
    public IValue rawValue(final VM vm) {
        final var lv = left.rawValue(vm);

        if (lv != null) {
            final var rv = right.rawValue(vm);
            if (rv != null) {
                return new Value<>(CoreLib.Pair, new Pair(lv, rv));
            }
        }

        return null;
    }

    @Override
    public IValue value(final VM vm) {
        final var lv = left.value(vm);

        if (lv != null) {
            final var rv = right.value(vm);
            if (rv != null) {
                return new Value<>(CoreLib.Pair, new Pair(lv, rv));
            }
        }

        return null;
    }
}