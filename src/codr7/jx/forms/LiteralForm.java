package codr7.jx.forms;

import codr7.jx.BaseForm;
import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;
import codr7.jx.ops.Put;

public class LiteralForm extends BaseForm {
    private final IValue value;

    public LiteralForm(final IValue value, final Loc loc) {
        super(loc);
        this.value = value;
    }

    public void emit(final VM vm, final int rResult) {
        vm.emit(Put.make(rResult, value, loc()));
    }

    public String dump(final VM vm) { return value.dump(vm); }

    public IValue value(final VM vm) { return value; }
}