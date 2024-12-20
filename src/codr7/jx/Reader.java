package codr7.jx;

import java.io.IOException;
import java.util.Deque;

public interface Reader {
    boolean read(VM vm, Input in, Deque<IForm> out, Location location);
}
