package codr7.jx.libs.csv.iters;

import codr7.jx.*;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.Core;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Iterator;

public class RecordReader implements Iter {
    final Iterator<CSVRecord> csv;

    public RecordReader(final Iterator<CSVRecord> csv) {
        this.csv = csv;
    }

    @Override public String dump(final VM vm) {
        return "(csv/Records " + csv + ")";
    }

    @Override public boolean next(final VM vm, final int rResult, final Loc loc) {
        try {
            if (!csv.hasNext()) { return false; }
            final var out = new ArrayList<IValue>();
            for (final var v: csv.next()) {  out.add(new Value<>(Core.stringType, v)); }
            vm.registers.set(rResult, new Value<>(Core.listType, new ArrayList<>(out)));
            return true;
        }
        catch (final Exception e) {
            throw new EvalError(e.toString(), loc);
        }
    }
}