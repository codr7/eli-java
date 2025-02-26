package codr7.eli.libs.core.iters;

import codr7.eli.*;
import codr7.eli.libs.CoreLib;

import java.util.*;

public final class MapEntries implements Iter {
    private final TreeMap<IValue, IValue> map;
    private final Iterator<Map.Entry<IValue, IValue>> iter;

    public MapEntries(final TreeMap<IValue, IValue> map) {
        this.map = map;
        iter = map.entrySet().iterator();
    }

    @Override
    public String dump(final VM vm) {
        return "(MapEntries " + iter + ")";
    }

    @Override
    public boolean next(final VM vm, final int rResult, final Loc loc) {
        if (!iter.hasNext()) { return false; }
        final var e = iter.next();

        if (rResult != -1) {
            vm.registers.set(rResult, new Value<>(CoreLib.pairType, new Pair(e.getKey(), e.getValue())));
        }

        return true;
    }
}