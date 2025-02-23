package codr7.eli.libs.core.iters;

import codr7.eli.*;

import java.util.ArrayList;

public final class ListItems implements Iter {
    private final ArrayList<IValue> list;
    private int i = 0;

    public ListItems(final ArrayList<IValue> list) {
        this.list = list;
    }

    @Override
    public String dump(final VM vm) {
        return "(ListItems " + i + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (i >= list.size()) { return false; }
        if (rResult != -1) { vm.registers.set(rResult, list.get(i)); }
        i++;
        return true;
    }
}