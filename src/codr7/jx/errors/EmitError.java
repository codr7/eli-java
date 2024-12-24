package codr7.jx.errors;

import codr7.jx.Error;
import codr7.jx.Loc;

public class EmitError extends Error {
    public EmitError(final String message, final Loc loc) { super(message, loc); }
}
