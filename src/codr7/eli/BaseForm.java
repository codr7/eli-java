package codr7.eli;

public abstract class BaseForm implements IForm {
    private final Loc loc;

    public BaseForm(final Loc loc) {
        this.loc = loc;
    }

    public Loc loc() {
        return loc;
    }
}
