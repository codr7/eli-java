package codr7.jx.readers;

import codr7.jx.*;
import codr7.jx.errors.ReadError;
import codr7.jx.forms.CallForm;

import java.util.ArrayDeque;
import java.util.Deque;

public class CallReader implements Reader {
    public static final CallReader instance = new CallReader();

    public boolean read(final VM vm, final Input in, final Deque<IForm> out, final Location location) {
        if (in.peek() != '(') { return false; }
        final var loc = location.dup();
        location.update(in.pop());
        final var body = new ArrayDeque<IForm>();

        for (; ; ) {
            WhitespaceReader.instance.read(vm, in, body, location);
            var c = in.peek();
            if (c == 0) { throw new ReadError("Unexpected end of call", location); }

            if (c == ')') {
                in.pop();
                break;
            }

            if (!vm.read(in, body, location)) { throw new ReadError("Unexpected end of call", location); }
        }

        out.addLast(new CallForm(body.toArray(new IForm[0]), loc));
        return true;
    }
}