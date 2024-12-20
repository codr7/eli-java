package codr7.jx.forms;

import codr7.jx.BaseForm;
import codr7.jx.IValue;
import codr7.jx.Location;
import codr7.jx.VM;
import codr7.jx.ops.Put;

public class LiteralForm extends BaseForm {
    private final IValue value;

    public LiteralForm(final IValue value, final Location location) {
        super(location);
        this.value = value;
    }

    public void emit(final VM vm, final int rResult) {
        vm.emit(Put.make(rResult, value, location()));
    }

    public String toString(final VM vm) { return value.dump(vm); }

    public IValue value(final VM vm) { return value; }
}