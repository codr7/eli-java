package codr7.eli.forms;

import codr7.eli.*;

public class QuoteForm extends BaseForm {
    public final IForm target;

    public QuoteForm(final IForm target, final Loc loc) {
        super(loc);
        this.target = target;
    }

    @Override
    public String argId(final VM vm, final Loc loc) {
        return "'" + target.argId(vm, loc);
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        value(vm).emit(vm, rResult, loc());
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof QuoteForm f) {
            return target.eq(f.target);
        }

        return false;
    }

    @Override
    public String dump(final VM vm) {
        return "'" + target.dump(vm);
    }

    @Override
    public IValue rawValue(final VM vm) {
        return target.quote(vm, loc());
    }

    @Override
    public void unquote(VM vm, int rResult, Loc loc) {
        target.unquote(vm, rResult, loc);
    }
}