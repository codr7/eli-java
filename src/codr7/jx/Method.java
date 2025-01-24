package codr7.jx;

import codr7.jx.errors.EvalError;
import codr7.jx.forms.CallForm;
import codr7.jx.forms.IdForm;
import codr7.jx.forms.LiteralForm;
import codr7.jx.libs.CoreLib;
import codr7.jx.ops.Copy;
import codr7.jx.ops.Goto;
import codr7.jx.ops.SetPath;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

import static codr7.jx.libs.CoreLib.bindingType;

public record Method(String id,
                     Arg[] args, int rArgs,
                     IType resultType, int rResult,
                     IForm[] body,
                     Label start, Label end) {
    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].id().endsWith("*")) { return -1; }
        return result;
    }

    public void emitBody(final VM vm, final int rArgs, final int rResult, final Loc loc) {
        final var start = vm.label(vm.emitPc());
        final var end = vm.label(-1);

        vm.doLib(null, () -> {
            vm.currentLib.bindMacro("recall", args, null,
                    (_vm, args, _rResult, _loc) -> {
                        final var rArgsCopy = vm.alloc(args.length);

                        for (var i = 0; i < args.length; i++) {
                            args[i].emit(vm, rArgsCopy+i);
                        }

                        for (var i = 0; i < args.length; i++) {
                            _vm.emit(new Copy(rArgsCopy+i, rArgs+i, loc));
                        }

                        _vm.emit(new Goto(start, _loc));
                    });

            vm.currentLib.bindMacro("return", args, null,
                    (_vm, args, _rResult, _loc) -> {
                        _vm.emit( new ArrayDeque<>(Arrays.asList(args)), _rResult);
                        _vm.emit(new Goto(end, _loc));
                    });

            vm.emit(new SetPath(vm.path, loc));

            for (var i = 0; i < args.length; i++) {
                final var ma = args[i];
                var aid = ma.id();

                if (aid.charAt(0) != '\'') {
                    vm.currentLib.bind(aid, bindingType, new Binding(null, rArgs + i));
                }
            }

            vm.emit(body, rResult);
        });

        end.pc = vm.emitPc();
    }

    public void call(final VM vm,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        if (start.pc == -1) {
            final var skip = vm.label(-1);
            vm.emit(new Goto(skip, loc));
            start.pc = vm.emitPc();
            emitBody(vm, rArgs(), rResult(), loc);
            end().pc = vm.emitPc();
            skip.pc = end().pc;
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

        if (resultType() != null && rResult != rResult()) {
            vm.registers.set(rResult, vm.registers.get(rResult()));
        }
    }

    public String dump(final VM vm) {
        return "(Method " + id + ")";
    }
}
