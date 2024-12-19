package codr7.jx;

public class ReadError extends Error {
    public ReadError(final String message, final Location location) { super(message, location); }
}
