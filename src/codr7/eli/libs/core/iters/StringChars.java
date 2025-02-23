package codr7.eli.libs.core.iters;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

public final class StringChars implements Iter {
    private final String s;
    private int i = 0;

    public StringChars(final String s) {
        this.s = s;
    }

    @Override
    public String dump(final VM vm) {
        return "(ListItems " + s + ' ' + i + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (i == s.length()) { return false; }
        if (rResult != -1) { vm.registers.set(rResult, new Value<>(CoreLib.charType, s.charAt(i))); }
        i++;
        return true;
    }
}