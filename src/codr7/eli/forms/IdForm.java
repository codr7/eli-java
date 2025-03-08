package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.libs.CoreLib;

public class IdForm extends BaseForm {
    public final String id;

    public IdForm(final String id, final Loc loc) {
        super(loc);
        this.id = id;
    }

    public static IValue get(final Lib lib, final String id, final Loc loc) {
        final var i = id.indexOf('/');

        if (i > 0) {
            final var lid = id.substring(0, i);
            final var l = lib.find(lid).cast(CoreLib.Lib);
            if (l == null) {
                throw new EmitError("Unknown id: " + l + "/" + lid, loc);
            }
            return get(l, id.substring(i + 1), loc);
        }

        final var v = lib.find(id);
        if (v == null) {
            throw new EmitError("Unknown id: " + id, loc);
        }
        return v;
    }

    @Override
    public String argId(final VM vm, final Loc loc) {
        return id;
    }

    @Override
    public void bindRegister(final VM vm, final int rValue, final Loc loc) {
        vm.currentLib.bind(id, new Value<>(CoreLib.Binding, new Binding(null, rValue)));
    }

    @Override
    public void bindValue(final VM vm, final IValue value, final Loc loc) {
        final var rValue = vm.alloc(1);
        vm.registers.set(rValue, value);
        vm.currentLib.bind(id, new Value<>(CoreLib.Binding, new Binding(value.type(), rValue)));
    }

    @Override
    public String dump(final VM vm) {
        return id;
    }

    @Override
    public void emit(final VM vm, final int rResult) {
        get(vm.currentLib, id, loc()).emit(vm, rResult, loc());
    }

    @Override
    public boolean eq(final IForm other) {
        if (other instanceof IdForm f) {
            return f.id.equals(id);
        }
        return false;
    }

    @Override
    public boolean isNil() {
        return id.equals("_");
    }

    @Override
    public IValue quote(final VM vm, final Loc loc) {
        return new Value<>(CoreLib.Sym, id);
    }

    @Override
    public IValue rawValue(final VM vm) {
        return get(vm.currentLib, id, loc());
    }
}