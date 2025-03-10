package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.TypeCheck;

public final class TypeForm extends BaseForm {
    public final IForm target;
    public final IForm type;

    public TypeForm(final IForm target, final IForm type, final Loc loc) {
        super(loc);
        this.target = target;
        this.type = type;
    }

    @Override
    public void bindRegister(final VM vm, final int rValue, final IType type, final Loc loc) {
        target.bindRegister(vm, rValue, getType(vm), loc);
    }

    @Override
    public void bindValue(final VM vm, final IValue value, final Loc loc) {
        value.checkType(vm, getType(vm), loc);
        target.bindValue(vm, value, loc);
    }

    @Override
    public String dump(final VM vm) {
        return target.dump(vm) + '@' + type.dump(vm);
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        target.emit(vm, rResult);
        vm.emit(new TypeCheck(rResult, getType(vm), loc()));
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof TypeForm f) {
            return target.eq(f.target) && type.eq(f.type);
        }

        return false;
    }

    private IType getType(final VM vm) {
        return type.value(vm).cast(CoreLib.Meta);
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        return target.quote(vm, loc);
    }

    @Override
    public IValue rawValue(final VM vm) {
        return target.rawValue(vm);
    }

    @Override
    public IValue value(final VM vm) {
        return target.value(vm);
    }
}