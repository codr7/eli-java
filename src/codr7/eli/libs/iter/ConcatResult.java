package codr7.eli.libs.iter;

import codr7.eli.Iter;
import codr7.eli.Loc;
import codr7.eli.VM;

public final class ConcatResult implements Iter {
    private final Iter[] in;
    private int i = 0;

    public ConcatResult(final Iter[] in) {
        this.in = in;
    }

    @Override
    public String dump(final VM vm) {
        return "(iter/ConcatResult " + in.length + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (i == in.length){
            return false;
        }

        if (in[i].next(vm, rResult, loc)) {
            return true;
        }

        i++;
        return next(vm, rResult, loc);
    }
}