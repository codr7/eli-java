package codr7.eli;

public class Error extends RuntimeException {
    public final Loc loc;

    public Error(final String message, final Loc loc) {
        super("Error in " + loc.toString() + ": " + message);
        this.loc = loc;
    }
}
