package codr7.eli.libs.csv.iters;

import codr7.eli.*;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;
import java.util.Iterator;

public final class RecordReader implements Iter {
    final Iterator<String[]> csv;

    public RecordReader(final Iterator<String[]> csv) {
        this.csv = csv;
    }

    @Override public String dump(final VM vm) {
        return "(csv/Records " + csv + ")";
    }

    @Override public boolean next(final VM vm, final int rResult, final Loc loc) {
        try {
            if (!csv.hasNext()) { return false; }
            final var out = new ArrayList<IValue>();

            for (final var v: csv.next()) {
                out.add(new Value<>(CoreLib.stringType, v
                        .replaceAll("\"", "")
                        .replaceAll(",", "")));
            }

            vm.registers.set(rResult, new Value<>(CoreLib.listType, new ArrayList<>(out)));
            return true;
        }
        catch (final Exception e) {
            throw new EvalError(e.toString(), loc);
        }
    }
}