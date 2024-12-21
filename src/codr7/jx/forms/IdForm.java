package codr7.jx.forms;

import codr7.jx.*;
import codr7.jx.errors.EmitError;
import codr7.jx.libs.Core;

public class IdForm extends BaseForm {
    public static IValue get(final Lib lib, final String id, final Location location) {
        final var i = id.indexOf('/');

        if (i != -1) {
            final var lid = id.substring(0, i);
            final var l = lib.find(lid).cast(Core.libType);
            if (l == null) { throw new EmitError("Unknown id: " + l + "/" + lid, location); }
            return get(l, id.substring(i+1), location);
        }

        final var v = lib.find(id);
        if (v == null) { throw new EmitError("Unknown id: " + id, location); }
        return v;
    }

    public final String id;

    public IdForm(final String id, final Location location) {
        super(location);
        this.id = id;
    }


    public void emit(final VM vm, final int rResult) {
        get(vm.currentLib, id, location()).emit(vm, rResult, location());
    }

    public boolean isNil() { return true; }
    public String toString(final VM vm) { return id; }
    public IValue value(final VM vm) { return get(vm.currentLib, id, location()); }
}