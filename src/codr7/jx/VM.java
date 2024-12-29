package codr7.jx;

import codr7.jx.compilers.ExtendGoto;
import codr7.jx.compilers.UnusedCopy;
import codr7.jx.compilers.UnusedPut;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.Core;
import codr7.jx.libs.GUI;
import codr7.jx.libs.core.types.CallTrait;
import codr7.jx.ops.*;
import codr7.jx.readers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static codr7.jx.OpCode.NOP;
import static codr7.jx.OpCode.STOP;

public final class VM {
    public final static int VERSION = 1;

    public final List<Compiler> compilers = new ArrayList<>();
    public final List<Reader> infixReaders = new ArrayList<>();
    public final List<Label> labels = new ArrayList<>();
    public final ArrayList<Op> ops = new ArrayList<>();
    public Path path = Paths.get("");
    public int pc = 0;
    public final List<Reader> readers = new ArrayList<>();
    public final ArrayList<IValue> registers = new ArrayList<>();

    public final Core coreLib = new Core();
    public final GUI guiLib = new GUI();
    public final Lib userLib = new Lib("user");

    public Lib currentLib = null;

    public VM() {
        readers.add(WhitespaceReader.instance);
        readers.add(CallReader.instance);
        readers.add(IntReader.instance);
        readers.add(IdReader.instance);
        readers.add(ListReader.instance);
        readers.add(StringReader.instance);
        infixReaders.add(PairReader.instance);

        compilers.add(ExtendGoto.instance);
        compilers.add(UnusedCopy.instance);
        compilers.add(UnusedPut.instance);

        userLib.bind(coreLib);
        userLib.bind(guiLib);
        currentLib = userLib;
    }

    public int alloc(final int n) {
        final var result = registers.size();
        for (var i = 0; i < n; i++) {
            registers.add(Core.NIL);
        }
        return result;
    }

    public void compile(final int startPc) {
        var done = false;

        while (!done) {
            done = true;

            for (final var c: compilers) {
                if (c.compile(this, startPc)) { done = false; }
            }
        }

        defragOps(startPc);
    }

    public void defragOps(final int startPc) {
        for (var i = startPc; i < ops.size();) {
            final var op = ops.get(i);

            if (op.code() == NOP) {
                ops.remove(i);

                for (final var l: labels) {
                    if (l.pc > i) {
                        l.pc--;
                    }
                }
            } else {
                i++;
            }
        }
    }

    public void doLib(final Lib lib, final DoLibBody body) {
        final var prevLib = currentLib;
        currentLib = new Lib((lib == null) ? prevLib : lib);

        try {
            body.call();
        } finally {
            currentLib = prevLib;
        }
    }

    public void dumpOps(final int startPc) {
        for (var i = startPc; i < ops.size(); i++) { System.out.printf("% 4d %s\n", i, ops.get(i).dump(this)); }
    }

    public int emit(final Op op) {
        final var pc = emitPc();
        ops.add(op);
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
        return ops.size();
    }

    public void eval(final int fromPc) {
        final var lop = ops.getLast();
        var stopPc = -1;
        if (lop.code() != STOP) {
            stopPc = emit(Stop.make(lop.loc()));
        }
        pc = fromPc;
        try {
            eval();
        } finally {
            if (stopPc != -1) {
                ops.set(stopPc, Nop.make(ops.get(stopPc).loc()));
            }
        }
    }

    public void eval(final int fromPc, final int toPc) {
        var prevOp = ops.get(toPc);
        ops.set(toPc, Stop.make(prevOp.loc()));
        pc = fromPc;

        try {
            eval();
        } finally {
            ops.set(toPc, prevOp);
        }
    }

    public void eval(final String in, final int rResult, final Loc loc) {
        final var skipPc = emit(Nop.make(loc));
        final var startPc = emit(read(in, loc), rResult);
        ops.set(skipPc, codr7.jx.ops.Goto.make(label(), loc));
        eval(startPc);
    }

    public void eval() {
        for (; ; ) {
            final Op op = ops.get(pc);

            switch (op.code()) {
                case ADD_ITEM: {
                    final var addOp = (AddItem) op.data();
                    final var t = registers.get(addOp.rTarget()).cast(Core.listType);
                    t.add(registers.get(addOp.rItem()));
                    pc++;
                    break;
                }
                case BENCH: {
                    final var benchOp = (Bench) op.data();
                    final var started = System.nanoTime();
                    eval(pc+1, benchOp.bodyEnd().pc);
                    final var elapsed = Duration.ofNanos(System.nanoTime() - started);
                    registers.set(benchOp.rResult(), new Value<>(Core.timeType, elapsed));
                    pc = benchOp.bodyEnd().pc;
                    break;
                }
                case BRANCH: {
                    final var branchOp = (Branch) op.data();
                    pc = registers.get(branchOp.rCondition()).toBit(this) ? pc + 1 : branchOp.elseStart().pc;
                    break;
                }
                case CALL_REGISTER: {
                    final var callOp = (CallRegister) op.data();
                    final var t = registers.get(callOp.rTarget());

                    if (t.type() instanceof CallTrait ct) {
                        pc++;
                        ct.call(this, t, callOp.rArguments(), callOp.arity(), callOp.rResult(), op.loc());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.loc());
                    }

                    break;
                }
                case CALL_VALUE: {
                    final var callOp = (CallValue) op.data();
                    final var t = callOp.target();

                    if (t.type() instanceof CallTrait ct) {
                        pc++;
                        ct.call(this, t, callOp.rArguments(), callOp.arity(), callOp.rResult(), op.loc());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.loc());
                    }

                    break;
                }
                case CHECK: {
                    final var checkOp = (Check) op.data();
                    final var expected = registers.get(checkOp.rValues());
                    final var actual = registers.get(checkOp.rValues() + 1);

                    if (!expected.equals(actual)) {
                        throw new EvalError("Check failed; expected " +
                                expected.dump(this) + ", actual: " + actual.dump(this),
                                op.loc());
                    }

                    pc++;
                    break;
                }
                case COPY: {
                    final var copyOp = (Copy) op.data();
                    registers.set(copyOp.rTo(), registers.get(copyOp.rFrom()));
                    pc++;
                    break;
                }
                case CREATE_LIST: {
                    final var createOp = (CreateList) op.data();
                    registers.set(createOp.rTarget(), new Value<>(Core.listType, new ArrayList<>()));
                    pc++;
                    break;
                }
                case DEC: {
                    final var decOp = (Dec) op.data();
                    final var v = registers.get(decOp.rTarget()).cast(Core.intType);
                    final var nv = new Value<>(Core.intType, v - 1);
                    registers.set(decOp.rTarget(), nv);
                    pc++;
                    break;
                }
                case GOTO: {
                    pc = ((codr7.jx.ops.Goto) op.data()).target().pc;
                    break;
                }
                case LEFT: {
                    final var leftOp = (Left) op.data();
                    registers.set(leftOp.rResult(), registers.get(leftOp.rPair()).cast(Core.pairType).left());
                    pc++;
                    break;
                }
                case NEXT: {
                    final var nextOp = (Next) op.data();
                    final var iter = registers.get(nextOp.rIter()).cast(Core.iterType);
                    pc = (iter.next(this, nextOp.rItem(), op.loc()))
                        ? pc + 1
                        : nextOp.bodyEnd().pc;
                    break;
                }
                case NOP: {
                    pc++;
                    break;
                }
                case PUT: {
                    final var putOp = (Put) op.data();
                    registers.set(putOp.rTarget(), putOp.value().dup(this));
                    pc++;
                    break;
                }
                case RIGHT: {
                    final var rightOp = (Right) op.data();
                    registers.set(rightOp.rResult(), registers.get(rightOp.rPair()).cast(Core.pairType).left());
                    pc++;
                    break;
                }
                case SET_PATH: {
                    final var setOp = (SetPath) op.data();
                    path = setOp.path();
                    pc++;
                    break;
                }
                case STOP: {
                    pc++;
                    return;
                }
                case ZIP: {
                    final var zipOp = (Zip) op.data();
                    final var left = registers.get(zipOp.rLeft());
                    final var right = registers.get(zipOp.rRight());
                    registers.set(zipOp.rResult(), new Value<>(Core.pairType, new Pair(left, right)));
                    pc++;
                    break;
                }
            }
        }
    }

    public Integer findRead(final int rTarget, final int startPc, final Set<Integer> skip) {
        final var r = new HashSet<Integer>();
        final var w = new HashSet<Integer>();
        for (var pc = startPc; pc < ops.size();) {
            if (skip.contains(pc)) { break; }
            skip.add(pc);
            final var op = ops.get(pc);
            op.io(this, r, w);
            if (r.contains(rTarget)) { return pc; }
            if (w.contains(rTarget)) { break; }

            if (op.data() == null) {
                pc ++;
            } else {
                switch (op.data()) {
                    case codr7.jx.ops.Goto gotoOp: {
                        final var result = findRead(rTarget, pc + 1, skip);
                        if (result != null) { return result; }
                        pc = gotoOp.target().pc;
                        break;
                    }
                    default:
                        pc++;
                        break;
                }
            }
        }

        return null;
    }

    public Label label(final int pc) {
        final var l = new Label(pc);
        labels.add(l);
        return l;
    }

    public Label label() {
        return label(emitPc());
    }

    public void load(final Path path, int rResult) {
        final var prevPath = this.path;
        final var p = prevPath.resolve(path);
        final var location = new Loc(p.toString());
        Deque<IForm> out;

        try {
            out = read(Files.readString(p), location);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        emit(SetPath.make(p.getParent(), location));
        final var startPc = emitPc();
        emit(out, rResult);
        emit(SetPath.make(prevPath, location));
        compile(startPc);
    }

    public boolean read(final Input in, final Deque<IForm> out, final Loc loc) {
        final var result = readers.stream().anyMatch(r -> r.read(this, in, out, loc));
        var _ = infixReaders.stream().anyMatch(r -> r.read(this, in, out, loc));
        return result;
    }

    public Deque<IForm> read(final String in, final Loc loc) {
        final var out = new ArrayDeque<IForm>();
        final var _in = new Input(in);
        while (read(_in, out, loc)) ;
        return out;
    }

    public interface DoLibBody {
        void call();
    }
}
