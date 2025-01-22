package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.ops.Put;

public class LiteralForm extends BaseForm {
    private final IValue value;

    public LiteralForm(final IValue value, final Loc loc) {
        super(loc);
        this.value = value;
    }

    @Override public void emit(final VM vm, final int rResult) {
        vm.emit(new Put(rResult, value, loc()));
    }

    @Override public boolean eq(final IForm other) {
        if (other instanceof LiteralForm f) {
            return f.value.eq(value);
        }

        return false;
    }

    @Override public String dump(final VM vm) { return value.dump(vm); }
    @Override public IValue quote(final VM vm, final Loc loc) { return value; }
    @Override public IValue rawValue(final VM vm) { return value; }
}