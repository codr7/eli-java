package codr7.eli.errors;

import codr7.eli.Error;
import codr7.eli.Loc;

public class EmitError extends Error {
    public EmitError(final String message, final Loc loc) { super(message, loc); }
}
