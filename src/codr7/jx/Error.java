package codr7.jx;

public class Error extends RuntimeException {
    public final Loc loc;

    public Error(final String message, final Loc loc) {
        super("Error in " + loc.toString() + ": " + message);
        this.loc = loc;
    }
}
