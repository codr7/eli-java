package codr7.jx.libs.core.types;

import codr7.jx.BaseType;
import codr7.jx.IType;
import codr7.jx.IValue;
import codr7.jx.VM;

public class TraitType extends BaseType<Void> {
    public TraitType(final String id, final IType...parentTypes) { super(id, parentTypes); }
    @Override public String dump(final VM vm, final IValue value) { return "?"; }
}