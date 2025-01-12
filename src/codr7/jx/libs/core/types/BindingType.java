package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.ops.Copy;

public class BindingType extends BaseType<Binding> {
    public BindingType(final String id) { super(id); }

    @Override public void emit(VM vm, IValue value, int rResult, Loc loc) {
        final var rValue = value.cast(this).rValue();
        if (rResult != rValue) { vm.emit(new Copy(rValue, rResult, loc)); }
    }
}
