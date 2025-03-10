package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;
import codr7.eli.forms.TypeForm;

import java.util.Deque;

public class TypeReader implements Reader {
    public static final TypeReader instance = new TypeReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '@') {
            return false;
        }

        final var floc = loc.dup();
        loc.update(in.pop());

        if (out.isEmpty() || !vm.read(in, out, loc)) {
            throw new ReadError("Invalid type expression", loc);
        }

        final var type = out.removeLast();
        final var target = out.removeLast();
        out.addLast(new TypeForm(target, type, floc));
        return true;
    }
}