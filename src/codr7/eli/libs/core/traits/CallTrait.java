package codr7.eli.libs.core.traits;

import codr7.eli.IForm;
import codr7.eli.IValue;
import codr7.eli.Loc;
import codr7.eli.VM;
import codr7.eli.forms.SplatForm;
import codr7.eli.libs.CoreLib;
import codr7.eli.ops.CallValue;

import java.util.ArrayList;
import java.util.List;

public interface CallTrait {
    static void expandArg(final VM vm,
                          final IValue value,
                          final List<IValue> out,
                          final Loc loc) {
        if (value.type() == CoreLib.splatType) {
            final var rValue = vm.alloc(1);
            final var it = value.cast(CoreLib.splatType);

            while (it.next(vm, rValue, loc)) {
                expandArg(vm, vm.registers.get(rValue), out, loc);
            }
        } else {
            out.add(value);
        }
    }

    default void call(final VM vm,
                      final IValue target,
                      final int rArgs,
                      final int arity,
                      final int rResult,
                      final boolean eval,
                      final Loc loc) {
        final var args = new ArrayList<IValue>();

        for (var i = 0; i < arity; i++) {
            final var v = vm.registers.get(rArgs+i);
            CallTrait.expandArg(vm, v, args, loc);
        }

        call(vm, target, args.toArray(IValue[]::new), rResult, eval, loc);
    }

    void call(final VM vm,
              final IValue target,
              final IValue[] args,
              final int rResult,
              final boolean eval,
              final Loc loc);

    default void emitCall(VM vm, IValue target, IForm[] body, int rResult, Loc loc) {
        final var arity = body.length - 1;
        final var rArgs = vm.alloc(arity);

        for (var i = 0; i < arity; i++) {
            final var f = body[i+1];
            f.emit(vm, rArgs + i);
        }

        vm.emit(new CallValue(target, rArgs, arity, rResult, loc));
    }
}
