package codr7.jx;

public class Error extends RuntimeException {
    public final Location location;

    public Error(final String message, final Location location) {
        super("Error in " + location.toString() + ": " + message);
        this.location = location;
    }
}
