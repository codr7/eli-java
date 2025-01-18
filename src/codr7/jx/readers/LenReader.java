package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.CallForm;
import codr7.jx.forms.IdForm;

import java.util.Deque;

public class LenReader implements Reader {
    public static final LenReader instance = new LenReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc loc) {
        if (in.peek() != '#') { return false; }
        final var floc = loc.dup();
        loc.update(in.pop());
        if (!vm.read(in, out, loc)) { throw new ReadError("Invalid len", loc); }
        out.addLast(new CallForm(new IForm[]{new IdForm("len", floc), out.removeLast()}, floc));
        return true;
    }
}