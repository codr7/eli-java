package codr7.eli;

import codr7.eli.errors.EvalError;
import codr7.eli.ops.Copy;
import codr7.eli.ops.Goto;

import java.util.ArrayDeque;
import java.util.Arrays;

import static codr7.eli.libs.CoreLib.bindingType;

public record Method(String id,
                     Arg[] args, int rArgs,
                     int rResult,
                     IForm[] body,
                     Label start, Label end) {

    public Method(String id,
                  Arg[] args, int rArgs,
                  int rResult,
                  IForm[] body) {
        this(id, args, rArgs, rResult, body, new Label(), new Label());
    }

    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].splat) { return -1; }
        return result;
    }

    public void call(final VM vm,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        if (start.pc == -1) {
            init(vm, loc);
        }

        if (arity() != -1 && arity < arity()) {
            throw new EvalError("Not enough args: " + dump(vm), loc);
        }

        if (rArgs != rArgs()) {
            for (var i = 0; i < arity; i++) {
                vm.registers.set(rArgs() + i, vm.registers.get(rArgs + i));
            }
        }

        vm.eval(start().pc, end().pc);

        if (rResult != rResult()) {
            vm.registers.set(rResult, vm.registers.get(rResult()));
        }
    }

    public String dump(final VM vm) {
        return "(Method " + id + ")";
    }

    public void emitBody(final VM vm, final int rArgs, final int rResult, final Loc loc) {
        final var start = new Label(vm.emitPc());
        final var end = new Label();

        vm.doLib(null, () -> {
            vm.currentLib.bindMacro("recall", args,
                    (_vm, args, _rResult, _loc) -> {
                        final var rArgsCopy = vm.alloc(args.length);

                        for (var i = 0; i < args.length; i++) {
                            args[i].emit(vm, rArgsCopy+i);
                        }

                        for (var i = 0; i < args.length; i++) {
                            _vm.emit(new Copy(rArgsCopy+i, rArgs+i, loc));
                        }

                        _vm.emit(new Goto(start));
                    });

            vm.currentLib.bindMacro("return", new Arg[]{new Arg("args*")},
                    (_vm, args, _rResult, _loc) -> {
                        _vm.doLib(null, () -> {
                            _vm.emit( new ArrayDeque<>(Arrays.asList(args)), _rResult);
                        });

                        _vm.emit(new Goto(end));
                    });

            for (var i = 0; i < args.length; i++) {
                final var ma = args[i];

                if (!ma.quote) {
                    vm.currentLib.bind(ma.id, bindingType, new Binding(null, rArgs + i));
                }
            }

            vm.emit(body, rResult);
        });

        end.pc = vm.emitPc();
    }

    private void init(final VM vm, final Loc loc) {
        final var skip = new Label();
        vm.emit(new Goto(skip));
        start.pc = vm.emitPc();
        emitBody(vm, rArgs(), rResult(), loc);
        end().pc = vm.emitPc();
        skip.pc = end().pc;
    }
}
