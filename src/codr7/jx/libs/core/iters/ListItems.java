package codr7.jx.libs.core.iters;

import codr7.jx.*;

import java.util.ArrayList;

public class ListItems implements Iter {
    private ArrayList<IValue> list;
    private int i = 0;

    public ListItems(final ArrayList<IValue> list) {
        this.list = list;
    }

    @Override public String dump(VM vm) {
        return "(ListItems " + i + ")";
    }

    @Override public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (i >= list.size()) { return false; }
        if (rResult != -1) { vm.registers.set(rResult, list.get(i)); }
        i++;
        return true;
    }
}