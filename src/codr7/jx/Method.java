package codr7.jx;

import codr7.jx.ops.Copy;
import codr7.jx.ops.Goto;

import java.util.ArrayDeque;
import java.util.Arrays;

import static codr7.jx.libs.Core.bindingType;

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

    public void emitBody(final VM vm, final int rResult, final Loc loc) {
        vm.doLib(() -> {
            for (var i = 0; i < args.length; i++) {
                final var ma = args[i];
                vm.currentLib.bind(ma.id(), bindingType, new Binding(null, rArgs + i));
            }

            vm.currentLib.bindMacro("recall", args, null,
                    (_vm, recallArgs, recallResult, _loc) -> {
                        final var rRecallArgs = vm.alloc(recallArgs.length);

                        for (var i = 0; i < recallArgs.length; i++) {
                            recallArgs[i].emit(vm, rRecallArgs + i);
                        }

                        for (var i = 0; i < recallArgs.length; i++) {
                            _vm.emit(Copy.make(rRecallArgs+i, rArgs+i, loc));
                        }

                        _vm.emit(Goto.make(start.pc, _loc));

                        if (recallResult != rResult) {
                            _vm.emit(Copy.make(recallResult, rResult, _loc));
                        }
                    });

            vm.emit(new ArrayDeque<>(Arrays.asList(body)), rResult);
        });
    }
}
