package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.traits.IterTrait;
import codr7.eli.libs.core.traits.SeqTrait;
import codr7.eli.ops.Splat;

public class SplatForm extends BaseForm {
    public final IForm target;

    public SplatForm(final IForm target, final Loc loc) {
        super(loc);
        this.target = target;
    }

    @Override public String argId(final VM vm, final Loc loc) {
        return target.argId(vm, loc) + '*';
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        final var v = value(vm);

        if (v == null) {
            target.emit(vm, rResult);
            vm.emit(new Splat(rResult));
        } else {
            v.emit(vm, rResult, loc());
        }
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof SplatForm f) {
            return target.eq(f.target);
        }

        return false;
    }

    @Override
    public String dump(final VM vm) {
        return target.dump(vm) + '*';
    }

    @Override
    public IValue rawValue(final VM vm) {
        final var v = target.value(vm);

        if (v != null && v.type() instanceof IterTrait t) {
            final var it = t.iter(vm, v);
            return new Value<>(CoreLib.splatType, it);
        }

        return null;
    }
}