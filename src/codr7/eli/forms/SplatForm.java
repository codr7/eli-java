package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;
import codr7.eli.libs.core.traits.IterableTrait;
import codr7.eli.ops.Splat;

public class SplatForm extends BaseForm {
    public final IForm target;

    public SplatForm(final IForm target, final Loc loc) {
        super(loc);
        this.target = target;
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        target.emit(vm, rResult);
        vm.emit(new Splat(rResult));
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

        if (v != null && v.type() instanceof IterableTrait t) {
            final var it = t.iter(vm, v);
            return new Value<>(CoreLib.Splat, it);
        }

        return null;
    }

    @Override
    public Arg toArg(final VM vm, final Loc loc) {
        final var ta = target.toArg(vm, loc);

        return new Arg(ta.id + '*', ta.type);
    }
}