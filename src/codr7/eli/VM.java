package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.libs.*;
import codr7.eli.libs.core.IterableTrait;
import codr7.eli.ops.*;
import codr7.eli.readers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public final class VM {
    public final static int VERSION = 21;
    public final CoreLib coreLib = new CoreLib(null);
    public final Lib homeLib = new Lib("home", null);
    public final List<Reader> prefixReaders = new ArrayList<>();
    public final List<Reader> suffixReaders = new ArrayList<>();
    private final ArrayList<Op> ops = new ArrayList<>();
    public final ArrayList<IValue> registers = new ArrayList<>();
    public final int rNull;
    private final List<Call> calls = new ArrayList<>();
    public boolean debug = false;
    public Lib currentLib = homeLib;
    public Path path = Paths.get("");
    public int pc = 0;

    public VM() {
        prefixReaders.add(WhitespaceReader.instance);
        prefixReaders.add(CallReader.instance);
        prefixReaders.add(CharReader.instance);
        prefixReaders.add(DecimalReader.instance);
        prefixReaders.add(IntReader.instance);
        prefixReaders.add(IdReader.instance);
        prefixReaders.add(CountReader.instance);
        prefixReaders.add(ListReader.instance);
        prefixReaders.add(MapReader.instance);
        prefixReaders.add(QuoteReader.instance);
        prefixReaders.add(StringReader.instance);
        prefixReaders.add(UnquoteReader.instance);

        suffixReaders.add(WhitespaceReader.instance);
        suffixReaders.add(PairReader.instance);
        suffixReaders.add(TypeReader.instance);
        suffixReaders.add(SplatReader.instance);

        rNull = alloc(1);

        homeLib.bind(new BitLib(homeLib));
        homeLib.bind(new CoreLib(homeLib));
        homeLib.bind(new GUILib(homeLib));
        homeLib.bind(new IntLib(homeLib));
        homeLib.bind(new IterLib(homeLib));
        homeLib.bind(new ListLib(homeLib));
        homeLib.bind(new SymLib(homeLib));
        homeLib.bind(new StringLib(homeLib));

        initLibs();
    }

    public int alloc(final int n) {
        final var result = registers.size();

        for (var i = 0; i < n; i++) {
            registers.add(CoreLib.NIL);
        }

        return result;
    }

    public void beginCall(final Method target, final int returnPc, final int rResult, final Loc loc) {
        calls.add(new Call(target, returnPc, rResult, loc));
    }

    public void doLib(final Lib lib, final DoLibBody body) {
        final var prevLib = currentLib;
        currentLib = (lib == null) ? new Lib(prevLib.id, prevLib) : lib;

        try {
            body.call();
        } finally {
            currentLib = prevLib;
        }
    }

    public void doLibId(final String id, final DoLibBody body) {
        final var v = currentLib.find(id);
        Lib lib;

        if (v == null) {
            lib = new Lib(id, currentLib);
            currentLib.bind(id, new Value<>(CoreLib.Lib, lib));
        } else {
            lib = v.cast(CoreLib.Lib);
        }

        doLib(lib, body);
    }

    public void dumpOps(final int startPc) {
        for (var i = startPc; i < ops.size(); i++) {
            System.out.printf("% 4d %s\n", i, ops.get(i).dump(this));
        }
    }

    public int emit(final Op op) {
        final var startPc = emitPc();
        ops.add(op);
        return startPc;
    }

    public int emitPc() {
        return ops.size();
    }

    public Call endCall() {
        return calls.removeLast();
    }

    public void eval(final int fromPc) {
        eval(fromPc, emitPc());
    }

    public void eval(final int fromPc, final int toPc) {
        Op prevOp;

        if (toPc == emitPc()) {
            emit(new Stop());
            prevOp = new Nop();
        } else {
            prevOp = ops.get(toPc);
            ops.set(toPc, new Stop());
        }

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
        final var skip = new Label();
        emit(new Goto(skip));
        final var startPc = emitPc();
        Form.emit(this, read(in, loc), rResult);
        skip.pc = emitPc();
        eval(startPc);
    }

    public void eval(final String in, final Loc loc) {
        eval(in, rNull, loc);
    }

    private void eval() {
        while (pc < ops.size()) {
            //System.out.println(pc + " " + ops.get(pc).dump(this));
            ops.get(pc).eval(this);
        }
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

            emit(new SetPath(p.getParent()));
            Form.emit(this, out, rResult);
            emit(new SetPath(prevPath));
        } finally {
            this.path = prevPath;
        }
    }

    public boolean read(final Input in, final Deque<IForm> out, final Loc loc) {
        final var result = prefixReaders.stream().anyMatch(r -> r.read(this, in, out, loc));
        var _ = suffixReaders.stream().anyMatch(r -> r.read(this, in, out, loc));
        return result;
    }

    public Deque<IForm> read(final String in, final Loc loc) {
        final var out = new ArrayDeque<IForm>();
        final var _in = new Input(in);
        while (read(_in, out, loc)) ;
        return out;
    }

    private void initLibs() {
        homeLib.importFrom(coreLib, new codr7.eli.Loc("vm"));

        for (final var v : homeLib.bindings.values()) {
            if (v.type() == CoreLib.Lib) {
                v.cast(CoreLib.Lib).tryInit(this);
            }
        }
    }

    public interface DoLibBody {
        void call();
    }
}
