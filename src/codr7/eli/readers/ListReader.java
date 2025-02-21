package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;
import codr7.eli.forms.ListForm;

import java.util.ArrayDeque;
import java.util.Deque;

public class ListReader implements Reader {
    public static final ListReader instance = new ListReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc location) {
        if (in.peek() != '[') { return false; }
        final var loc = location.dup();
        location.update(in.pop());
        final var body = new ArrayDeque<IForm>();

        for (; ; ) {
            WhitespaceReader.instance.read(vm, in, body, location);
            var c = in.peek();
            if (c == 0) { throw new ReadError("Unexpected end of list", location); }

            if (c == ']') {
                in.pop();
                break;
            }

            if (!vm.read(in, body, location)) { throw new ReadError("Unexpected end of list", location); }
        }

        out.addLast(new ListForm(body.toArray(new IForm[0]), loc));
        return true;
    }
}