package codr7.jx.errors;

import codr7.jx.Error;
import codr7.jx.Loc;

public class EvalError extends Error {
    public EvalError(final String message, final Loc loc) { super(message, loc); }
}
