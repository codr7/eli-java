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
                     IForm[] body) {
    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].splat) { return -1; }
        return result;
    }

    public void emitBody(final VM vm, final int rArgs, final int rResult, final Loc loc) {
        final var start = vm.label(vm.emitPc());
        final var end = vm.label(-1);

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

            vm.currentLib.bindMacro("return", args,
                    (_vm, args, _rResult, _loc) -> {
                        _vm.doLib(null, () -> {
                            _vm.emit( new ArrayDeque<>(Arrays.asList(args)), _rResult);
                        });

                        _vm.emit(new Goto(end));
                    });

            for (var i = 0; i < args.length; i++) {
                final var ma = args[i];
                if (!ma.quote) { vm.currentLib.bind(ma.id, bindingType, new Binding(null, rArgs + i)); }
            }

            vm.emit(body, rResult);
        });

        end.pc = vm.emitPc();
    }

    public String dump(final VM vm) {
        return "(Method " + id + ")";
    }
}
