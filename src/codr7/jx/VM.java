package codr7.jx;

import codr7.jx.libs.Core;
import codr7.jx.libs.core.types.CallTrait;
import codr7.jx.ops.*;
import codr7.jx.readers.CallReader;
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
    public Lib currentLib = null;

    public VM() {
        readers.add(WhitespaceReader.instance);
        readers.add(CallReader.instance);
        readers.add(IntReader.instance);
        readers.add(IdReader.instance);

        userLib.bind(coreLib);
        currentLib = userLib;
    }

    public int alloc(final int n) {
        final var result = registers.size();
        for (var i = 0; i < n; i++) {
            registers.add(Core.NIL);
        }
        return result;
    }

    public interface DoLibBody { void call(); }

    public void doLib(final DoLibBody body) {
        final var prevLib = currentLib;
        currentLib = new Lib(prevLib);
        try { body.call(); }
        finally { currentLib = prevLib; }
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

                    if (t.type() instanceof CallTrait ct) {
                        pc += 1;
                        ct.call(this, t, callOp.rArguments(), callOp.arity(), callOp.rResult(), op.location());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.location());
                    }

                    break;
                case COPY:
                    final var copyOp = (Copy) op.data();
                    registers.set(copyOp.rTo(), registers.get(copyOp.rFrom()));
                    pc++;
                    break;
                case LEFT:
                    final var leftOp = (Left)op.data();
                    registers.set(leftOp.rResult(), registers.get(leftOp.rPair()).cast(Core.pairType).left());
                    pc++;
                case PUT:
                    final var putOp = (Put) op.data();
                    registers.set(putOp.rTarget(), putOp.value().dup(this));
                    pc++;
                    break;
                case RIGHT:
                    final var rightOp = (Right)op.data();
                    registers.set(rightOp.rResult(), registers.get(rightOp.rPair()).cast(Core.pairType).left());
                    pc++;
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

    public boolean read(final Input in, final Deque<IForm> out, final Location location) {
        final var result = readers.stream().anyMatch(r -> r.read(this, in, out, location));
        var _ = infixReaders.stream().anyMatch(r -> r.read(this, in, out, location));
        return result;
    }

    public Deque<IForm> read(final String in, final Location location) {
        final var out = new ArrayDeque<IForm>();
        final var _in = new Input(in);
        while (read(_in, out, location));
        return out;
    }
}
