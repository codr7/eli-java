package codr7.jx;

import codr7.jx.libs.Core;
import codr7.jx.libs.core.types.CallTrait;
import codr7.jx.ops.*;
import codr7.jx.readers.IdReader;
import codr7.jx.readers.IntReader;
import codr7.jx.readers.WhitespaceReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static codr7.jx.OpCode.STOP;

public final class VM {
    public static final int VERSION = 1;

    public final List<Reader> infixReaders = new ArrayList<>();
    public final ArrayList<Op> ops = new ArrayList<>();
    public int pc = 0;
    public final List<Reader> readers = new ArrayList<>();
    public final ArrayList<IValue> registers = new ArrayList<>();

    public final Core coreLib = new Core();
    public final Lib userLib = new Lib("user");
    public Lib currentLib = userLib;

    public VM() {
        readers.add(WhitespaceReader.instance);
        readers.add(IntReader.instance);
        readers.add(IdReader.instance);

        userLib.bind(coreLib);
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
        ops.add(op);
        return pc;
    }

    public int emit(final Deque<IForm> in, final int rResult) {
        final var startPc = emitPc();
        for (final var f : in) { f.emit(this, rResult); }
        return startPc;
    }

    public int emitPc() {
        return ops.size();
    }

    public void eval(final int fromPc) {
        if (ops.getLast().code() != STOP) { emit(Stop.make(null)); }
        pc = fromPc;

        for (; ; ) {
            final Op op = ops.get(pc);
            System.out.printf("% 4d %s\n", pc, op.toString(this));

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
                    pc++;
                    break;
                case PUT:
                    final var putOp = (Put) op.data();
                    registers.set(putOp.rTarget(), putOp.value().dup(this));
                    pc++;
                    break;
                case STOP:
                    pc++;
                    return;
            }
        }
    }

    public IValue eval(final String in, final Location location) {
        final var rResult = alloc(1);
        eval(emit(read(in, location), rResult));
        return registers.get(rResult);
    }

    public Deque<IForm> read(final String in, final Location location) {
        final var out = new ArrayDeque<IForm>();
        final var input = new Input(in);

        while (readers.stream().anyMatch(r -> r.read(input, out, location))) {
            var _ = infixReaders.stream().anyMatch(r -> r.read(input, out, location));
        }

        return out;
    }
}
