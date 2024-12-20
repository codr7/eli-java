package codr7.jx;

import codr7.jx.libs.Core;
import codr7.jx.libs.core.types.CallTrait;
import codr7.jx.ops.Branch;
import codr7.jx.ops.Call;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Put;
import codr7.jx.readers.IdReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class VM {
    public final ArrayList<Op> code = new ArrayList<>();
    public final List<Reader> infixReaders = new ArrayList<>();
    public int pc = 0;
    public final List<Reader> readers = new ArrayList<>();
    public final ArrayList<IValue> registers = new ArrayList<>();

    public final Core coreLib = new Core();
    public final Lib userLib = new Lib("user");

    public Lib currentLib = userLib;

    public VM() {
        userLib.bind(coreLib);
        readers.add(IdReader.instance);
    }

    public int alloc(final int n) {
        final var result = registers.size();
        for (var i = 0; i < n; i++) {
            registers.add(Core.NIL);
        }
        return result;
    }

    public int emit(final Op op) {
        final var pc = emitPc();
        code.add(op);
        return pc;
    }

    public int emit(final Deque<IForm> in, final int rResult) {
        final var startPc = emitPc();
        for (final var f : in) {
            f.emit(this, rResult);
        }
        return startPc;
    }

    public int emitPc() {
        return code.size();
    }

    public void eval(final int fromPc) {
        pc = fromPc;

        for (; ; ) {
            final Op op = code.get(pc);

            switch (op.code()) {
                case BRANCH:
                    final var branchOp = (Branch) op.data();
                    pc = registers.get(branchOp.rCondition()).toBit(this) ? pc + 1 : branchOp.endPc();
                    break;
                case CALL:
                    final var callOp = (Call) op.data();
                    final var t = registers.get(callOp.rTarget());

                    if (t.type() instanceof CallTrait) {
                        pc += 1;
                        ((CallTrait)t.type()).call(this, t, callOp.rArguments(), callOp.rResult(), op.location());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.location());
                    }

                    break;
                case COPY:
                    final var copyOp = (Copy) op.data();
                    registers.set(copyOp.rTo(), registers.get(copyOp.rFrom()));
                    break;
                case PUT:
                    final var putOp = (Put) op.data();
                    registers.set(putOp.rTarget(), putOp.value().copy(this));
                    break;
                case STOP:
                    pc++;
                    return;
            }
        }
    }

    public IValue eval(final String in) {
        final var rResult = alloc(1);
        eval(emit(read(in), rResult));
        return registers.get(rResult);
    }

    public Deque<IForm> read(final String in) {
        final var out = new ArrayDeque<IForm>();
        final var location = new Location("read");
        final var input = new Input(in);

        while (readers.stream().anyMatch(r -> r.read(input, out, location))) {
            var _ = infixReaders.stream().anyMatch(r -> r.read(input, out, location));
        }

        return out;
    }
}
