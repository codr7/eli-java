package codr7.eli;

import codr7.eli.errors.EmitError;
import codr7.eli.errors.EvalError;

import java.util.Arrays;

import static codr7.eli.libs.CoreLib.Binding;

public final class Method implements IMethod {
    public final String id;
    public final int rArgs;
    public final int rResult;
    public final int minArity;
    public final int maxArity;

    public Method(final String id,
                  final Arg[] args, final int rArgs,
                  final int rResult,
                  final Label start, final Label end,
                  final Loc loc) {
        this.id = id;
        this.args = args;
        this.rArgs = rArgs;
        this.rResult = rResult;
        this.start = start;
        this.end = end;
        this.loc = loc;
        this.minArity = Arg.minArity(args);
        this.maxArity = Arg.maxArity(args);
        this.weight = Arg.weight(args);
    }

    @Override
    public Arg[] args() {
        return args;
    }

    @Override
    public int minArity() {
        return minArity;
    }

    @Override
    public int maxArity() {
        return maxArity;
    }

    public void bindArgs(final VM vm) {
        for (var i = 0; i < args.length; i++) {
            final var ma = args[i];
            vm.currentLib.bind(ma.id, Binding, new Binding(null, rArgs + i));
        }
    }

    public void call(final VM vm,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        if (args.length < minArity || args.length > maxArity) {
            throw new EmitError("Wrong number of args: " + this, loc);
        }

        var rArg = this.rArgs;
        var ai = 0;

        for (final var a : this.args) {
            ai = a.bind(vm, args, ai, rArg, loc);
            rArg++;
        }

        if (eval) {
            vm.eval(start.pc, end.pc);

            if (rResult != this.rResult) {
                vm.registers.set(rResult, vm.registers.get(this.rResult));
            }
        } else {
            vm.beginCall(this, vm.pc, rResult, loc);
            vm.pc = start.pc;
        }
    }

    public String dump(final VM vm) {
        return "(^" + id + "[?])";
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int weight() {
        return weight;
    }

    private final Arg[] args;
    private final Label start;
    private final Label end;
    private final Loc loc;
    private final int weight;
}
