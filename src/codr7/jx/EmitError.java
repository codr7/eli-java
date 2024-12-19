package codr7.jx;

public class EmitError extends Error {
    public EmitError(final String message, final Location location) { super(message, location); }
}
