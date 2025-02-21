package codr7.eli;

public class TraitType extends BaseType<Void> {
    public TraitType(final String id, final IType...parentTypes) { super(id, parentTypes); }
    @Override public String dump(final VM vm, final IValue value) { return "?"; }
}