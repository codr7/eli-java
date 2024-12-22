package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.ops.CallValue;

public class JMethodType extends BaseType<JMethod> implements CallTrait {
    public JMethodType(final String id) { super(id); }

    public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Location location) {
        target.cast(this).call(vm, rArgs, arity, rResult, location);
    }

    public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Location location) {
        final var arity = body.length - 1;
        final var rArgs = vm.alloc(arity);
        for (var i = 0; i < arity; i++) { body[i+1].emit(vm, rArgs + i); }
        vm.emit(CallValue.make(target, rArgs, arity, rResult, location));
    }
}
