package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Put;

public class BindingType extends BaseType<Binding> {
    public BindingType(final String id) { super(id); }

    public void emit(VM vm, IValue value, int rResult, Location location) {
        vm.emit(Copy.make(value.cast(this).rValue(), rResult, location));
    }
}
