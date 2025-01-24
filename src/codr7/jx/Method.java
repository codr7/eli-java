package codr7.jx;

import codr7.jx.ops.Copy;
import codr7.jx.ops.Goto;
import codr7.jx.ops.SetPath;

import java.util.ArrayDeque;
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
            vm.emit(new SetPath(vm.path, loc));

            for (var i = 0; i < args.length; i++) {
                final var ma = args[i];
                vm.currentLib.bind(ma.id(), bindingType, new Binding(null, rArgs + i));
            }

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

            vm.emit(new ArrayDeque<>(Arrays.asList(body)), rResult);
        });

        end.pc = vm.emitPc();
    }
}
