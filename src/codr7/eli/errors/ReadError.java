package codr7.eli.errors;

import codr7.eli.Error;
import codr7.eli.Loc;

public class ReadError extends Error {
    public ReadError(final String message, final Loc loc) {
        super(message, loc);
    }
}
