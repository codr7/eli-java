package codr7.jx;

public abstract class BaseForm implements IForm {
    private final Location location;

    public BaseForm(final Location location) {
        this.location = location;
    }

    public Location location() { return location; }
}
