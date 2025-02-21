package codr7.eli;

import java.util.Deque;

public interface Reader {
    boolean read(VM vm, Input in, Deque<IForm> out, Loc loc);
}
