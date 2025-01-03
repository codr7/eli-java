package codr7.jx.forms;

import codr7.jx.*;

public class QuoteForm extends BaseForm {
    public final IForm target;

    public QuoteForm(final IForm target, final Loc loc) {
        super(loc);
        this.target = target;
    }

    @Override public void emit(final VM vm, final int rResult) {
        value(vm).emit(vm, rResult, loc());
    }

    @Override public boolean eq(final IForm other) {
        if (other instanceof QuoteForm f) {
            return target.eq(f.target);
        }

        return false;
    }

    @Override public String dump(VM vm) {
        return "'" + target.dump(vm);
    }

    @Override public IValue value(VM vm) {
        return target.quote(vm, loc());
    }
}