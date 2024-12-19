package codr7.jx;

import java.io.IOException;
import java.util.Deque;

public interface Reader {
    boolean read(final Input in, final Deque<IForm> out, final Location location);
}
