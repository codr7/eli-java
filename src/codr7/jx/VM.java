package codr7.jx;

import codr7.jx.compilers.ExtendGoto;
import codr7.jx.compilers.UnusedCopy;
import codr7.jx.compilers.UnusedPut;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.CSVLib;
import codr7.jx.libs.CoreLib;
import codr7.jx.libs.GUILib;
import codr7.jx.libs.StringLib;
import codr7.jx.libs.core.traits.CallTrait;
import codr7.jx.libs.core.traits.SeqTrait;
import codr7.jx.ops.*;
import codr7.jx.readers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

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

    public final CoreLib coreLib = new CoreLib();
    public final CSVLib csvLib = new CSVLib();
    public final GUILib guiLib = new GUILib();
    public final StringLib stringLib = new StringLib();
    public final Lib userLib = new Lib("user");

    public Lib currentLib = null;

    public VM() {
        readers.add(WhitespaceReader.instance);
        readers.add(CallReader.instance);
        readers.add(CharReader.instance);
        readers.add(FixReader.instance);
        readers.add(IntReader.instance);
        readers.add(IdReader.instance);
        readers.add(ListReader.instance);
        readers.add(QuoteReader.instance);
        readers.add(StringReader.instance);
        infixReaders.add(PairReader.instance);

        compilers.add(ExtendGoto.instance);
        compilers.add(UnusedCopy.instance);
        compilers.add(UnusedPut.instance);

        userLib.bind(coreLib);
        userLib.bind(csvLib);
        userLib.bind(guiLib);
        userLib.bind(stringLib);
        currentLib = userLib;
    }

    public int alloc(final int n) {
        final var result = registers.size();
        for (var i = 0; i < n; i++) {
            registers.add(CoreLib.NIL);
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

            if (defragOps(startPc)) { done = false; }
        }
    }

    public boolean defragOps(final int startPc) {
        var changed = false;

        for (var i = startPc; i < ops.size();) {
            final var op = ops.get(i);

            if (op instanceof Nop) {
                ops.remove(i);

                for (final var l: labels) {
                    if (l.pc > i) {
                        l.pc--;
                    }
                }

                changed = true;
            } else {
                i++;
            }
        }

        return changed;
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
        var stopPc = (lop instanceof Stop) ? ops.size()-1 : emit(new Stop());
        final var prevPc = pc;
        pc = fromPc;

        try {
            eval();
        } finally {
            if (stopPc != -1) {
                ops.set(stopPc, new Nop());
            }

            pc = prevPc;
        }
    }

    public void eval(final int fromPc, final int toPc) {
        var prevOp = ops.get(toPc);
        ops.set(toPc, new Stop());
        final var prevPc = pc;
        pc = fromPc;

        try {
            eval();
        } finally {
            ops.set(toPc, prevOp);
            pc = prevPc;
        }
    }

    public void eval(final String in, final int rResult, final Loc loc) {
        final var skipPc = emit(new Nop());
        final var startPc = emit(read(in, loc), rResult);
        ops.set(skipPc, new Goto(label(), loc));
        eval(startPc);
    }

    public void eval() {
        for (; ; ) {
            switch (ops.get(pc)) {
                case AddItem op: {
                    final var t = registers.get(op.rTarget()).cast(CoreLib.listType);
                    t.add(registers.get(op.rItem()));
                    pc++;
                    break;
                }
                case Bench op: {
                    final var started = System.nanoTime();
                    eval(pc+1, op.bodyEnd().pc);
                    final var elapsed = Duration.ofNanos(System.nanoTime() - started);
                    registers.set(op.rResult(), new Value<>(CoreLib.timeType, elapsed));
                    pc = op.bodyEnd().pc;
                    break;
                }
                case Branch op: {
                    pc = registers.get(op.rCondition()).toBit(this) ? pc + 1 : op.elseStart().pc;
                    break;
                }
                case CallRegister op: {
                    final var t = registers.get(op.rTarget());

                    if (t.type() instanceof CallTrait ct) {
                        pc++;
                        ct.call(this, t, op.rArguments(), op.arity(), op.rResult(), op.loc());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.loc());
                    }

                    break;
                }
                case CallValue op: {
                    final var t = op.target();

                    if (t.type() instanceof CallTrait ct) {
                        pc++;
                        ct.call(this, t, op.rArgs(), op.arity(), op.rResult(), op.loc());
                    } else {
                        throw new EvalError("Call not supported: " + t.dump(this), op.loc());
                    }

                    break;
                }
                case Check op: {
                    final var expected = registers.get(op.rValues());
                    final var actual = registers.get(op.rValues() + 1);

                    if (!expected.eq(actual)) {
                        throw new EvalError("Check failed; expected " +
                                expected.dump(this) + ", actual: " + actual.dump(this),
                                op.loc());
                    }

                    pc++;
                    break;
                }
                case Copy op: {
                    registers.set(op.rTo(), registers.get(op.rFrom()));
                    pc++;
                    break;
                }
                case CreateIter op: {
                    final var t = registers.get(op.rTarget());

                    if (t.type() instanceof SeqTrait st) {
                        final var it = st.iter(this, t, op.loc());
                        registers.set(op.rTarget(), new Value<>(CoreLib.iterType, it));
                    } else {
                        throw new EvalError("Expected seq: " + t.dump(this), op.loc());
                    }

                    pc++;
                    break;
                }
                case CreateList op: {
                    registers.set(op.rTarget(), new Value<>(CoreLib.listType, new ArrayList<>()));
                    pc++;
                    break;
                }
                case Dec op: {
                    final var v = registers.get(op.rTarget()).cast(CoreLib.intType);
                    final var dv = (op.rDelta() == -1) ? 1L : registers.get(op.rDelta()).cast(CoreLib.intType);
                    registers.set(op.rTarget(), new Value<>(CoreLib.intType, v - dv));
                    pc++;
                    break;
                }
                case Goto op: {
                    pc = op.target().pc;
                    break;
                }
                case Inc op: {
                    final var v = registers.get(op.rTarget()).cast(CoreLib.intType);
                    final var dv = (op.rDelta() == -1) ? 1L : registers.get(op.rDelta()).cast(CoreLib.intType);
                    registers.set(op.rTarget(), new Value<>(CoreLib.intType, v + dv));
                    pc++;
                    break;
                }
                case Left op: {
                    registers.set(op.rResult(), registers.get(op.rPair()).cast(CoreLib.pairType).left());
                    pc++;
                    break;
                }
                case Next op: {
                    final var iter = registers.get(op.rIter()).cast(CoreLib.iterType);
                    pc = (iter.next(this, op.rItem(), op.loc()))
                        ? pc + 1
                        : op.bodyEnd().pc;
                    break;
                }
                case Nop _: {
                    pc++;
                    break;
                }
                case Put op: {
                    registers.set(op.rTarget(), op.value().dup(this));
                    pc++;
                    break;
                }
                case Right op: {
                    registers.set(op.rResult(), registers.get(op.rPair()).cast(CoreLib.pairType).left());
                    pc++;
                    break;
                }
                case SetPath op: {
                    path = op.path();
                    pc++;
                    break;
                }
                case Stop _: {
                    pc++;
                    return;
                }
                case Trace op:
                    System.out.println(op.text());
                    pc++;
                    break;
                case Zip op: {
                    final var left = registers.get(op.rLeft());
                    final var right = registers.get(op.rRight());
                    registers.set(op.rResult(), new Value<>(CoreLib.pairType, new Pair(left, right)));
                    pc++;
                    break;
                }
                case Op op:
                    throw new RuntimeException("Invalid op: " + op.dump(this));
            }
        }
    }

    public Integer findRead(final int rTarget, final int startPc, final HashSet<Integer> skip) {
        final var r = new HashSet<Integer>();
        final var w = new HashSet<Integer>();

        for (var pc = startPc; pc < ops.size();) {
            while (skip.contains(pc)) { pc++; }
            if (pc == ops.size()) { break; }
            skip.add(pc);
            final var op = ops.get(pc);
            op.io(this, r, w);
            if (r.contains(rTarget)) { return pc; }
            if (w.contains(rTarget)) { break; }

                switch (op) {
                    case Branch branchOp: {
                        final var result = findRead(rTarget, branchOp.elseStart().pc, skip);
                        if (result != null) { return result; }
                        pc++;
                        break;
                    }
                    case Goto gotoOp: {
                        final var result = findRead(rTarget, gotoOp.target().pc, skip);
                        if (result != null) { return result; }
                        pc++;
                        break;
                    }
                    default:
                        pc++;
                        break;
                }
            }

        return null;
    }

    public Integer findRead(final int rTarget, final int startPc, final int...skip) {
        final var ss = new HashSet<Integer>();
        for (final var s: skip) { ss.add(s); }
        return findRead(rTarget, startPc, ss);
    }

    public Integer findWrite(final int rTarget, final int startPc, final HashSet<Integer> skip) {
        final var r = new HashSet<Integer>();
        final var w = new HashSet<Integer>();

        for (var pc = startPc; pc < ops.size();) {
            while (skip.contains(pc)) { pc++; }
            if (pc == ops.size()) { break; }
            skip.add(pc);
            final var op = ops.get(pc);
            op.io(this, r, w);
            if (w.contains(rTarget)) { return pc; }

            switch (op) {
                case Branch branchOp: {
                    final var result = findRead(rTarget, branchOp.elseStart().pc, skip);
                    if (result != null) { return result; }
                    pc++;
                    break;
                }
                case Goto gotoOp: {
                    final var result = findRead(rTarget, gotoOp.target().pc, skip);
                    if (result != null) { return result; }
                    pc++;
                    break;
                }
                default:
                    pc++;
                    break;
            }
        }

        return null;
    }

    public Integer findWrite(final int rTarget, final int startPc, final int...skip) {
        final var ss = new HashSet<Integer>();
        for (final var s: skip) { ss.add(s); }
        return findWrite(rTarget, startPc, ss);
    }

    public Label label(final int pc) {
        final var l = new Label(pc);
        labels.add(l);
        return l;
    }

    public Label label() {
        return label(emitPc());
    }

    public void load(final Path path, final int rResult) {
        final var prevPath = this.path;
        final var p = prevPath.resolve(path);
        final var location = new Loc(p.toString());
        this.path = p.getParent();

        try {
            final Deque<IForm> out;

            try {
                out = read(Files.readString(p), location);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            final var startPc = emitPc();
            emit(new SetPath(p.getParent(), location));
            emit(out, rResult);
            emit(new SetPath(prevPath, location));
            compile(startPc);
        } finally {
            this.path = prevPath;
        }
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
