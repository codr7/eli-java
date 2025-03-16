package codr7.eli.readers;

import codr7.eli.*;
import codr7.eli.errors.ReadError;
import codr7.eli.forms.MapForm;
import codr7.eli.forms.PairForm;
import codr7.eli.forms.QuoteForm;

import java.util.ArrayDeque;
import java.util.Deque;

public class MapReader implements Reader {
    public static final MapReader instance = new MapReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Loc location) {
        if (in.peek() != '{') {
            return false;
        }
        final var loc = location.dup();
        location.update(in.pop());
        final var body = new ArrayDeque<IForm>();

        for (; ; ) {
            WhitespaceReader.instance.read(vm, in, out, location);
            var c = in.peek();

            if (c == 0) {
                throw new ReadError("Unexpected end of map", location);
            }

            if (c == '}') {
                in.pop();
                break;
            }

            if (!vm.read(in, out, location)) {
                throw new ReadError("Unexpected end of map", location);
            }

            body.add(out.removeLast());
        }

        out.addLast(new MapForm(body.toArray(new IForm[0]), loc));
        return true;
    }
}