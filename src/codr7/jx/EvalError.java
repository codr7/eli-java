package codr7.jx;

public class EvalError extends Error {
    public EvalError(final String message, final Location location) { super(message, location); }
}
