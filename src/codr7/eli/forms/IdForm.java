package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.libs.CoreLib;

public class IdForm extends BaseForm {
    public record Result(Lib lib, String id) {}

    public static Result find(final Lib lib, final String id, final Loc loc) {
        final var i = id.indexOf('/');

        if (i > 0) {
            final var lid = id.substring(0, i);
            final var lv = lib.find(lid);

            if (lv == null) {
                return null;
            }

            final var l = lv.cast(CoreLib.Lib);

            if (l == null) {
                return null;
            }

            return find(l, id.substring(i + 1), loc);
        }

        return new Result(lib, id);
    }

    public static IValue get(final Lib lib, final String id, final Loc loc) {
        final var found = find(lib, id, loc);

        if (found == null) {
            throw new EmitError("Unknown id: " + lib.id + '/' + id, loc);
        }

        return found.lib.find(found.id);
    }

    public final String id;

    public IdForm(final String id, final Loc loc) {
        super(loc);
        this.id = id;
    }

    @Override
    public Arg toArg(final VM vm, final Loc loc) {
        return new Arg(id);
    }

    @Override
    public void bindRegister(final VM vm, final int rValue, final IType type, final Loc loc) {
        vm.currentLib.bind(id, new Value<>(CoreLib.Binding, new Binding(type, rValue)));
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