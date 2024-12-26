package codr7.jx.libs.core.types;

import codr7.jx.*;
import codr7.jx.errors.EvalError;
import codr7.jx.ops.Copy;

public class MethodType extends BaseType<Method> implements CallTrait {
    public MethodType(final String id) {
        super(id);
    }

    @Override public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        final var m = target.cast(this);
        final var ma = m.arity();
        if (ma != -1 && arity < ma) {
            throw new EvalError("Not enough args: " + target.dump(vm), loc);
        }

        if (rArgs != m.rArgs()) {
            for (var i = 0; i < arity; i++) {
                vm.registers.set(m.rArgs() + i, vm.registers.get(rArgs + i));
            }
        }

        vm.eval(m.startPc(), m.endPc());

        if (m.resultType() != null && rResult != m.rResult()) {
            vm.registers.set(rResult, vm.registers.get(m.rResult()));
        }
    }

    @Override public String dump(final VM vm, final IValue value) {
        return "(Method " + value.cast(this).id() + ")";
    }

    @Override public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Loc loc) {
        final var m = target.cast(this);
        final var arity = body.length - 1;

        for (var i = 0; i < arity; i++) {
            body[i + 1].emit(vm, m.rArgs() + i);
        }

        final var deltaPc = vm.emitPc() - m.startPc();

        for (var i = m.startPc(); i < m.endPc(); i++) {
            vm.emit(vm.ops.get(i).relocate(deltaPc));
        }

        if (m.resultType() != null && rResult != m.rResult()) {
            vm.emit(Copy.make(m.rResult(), rResult, 1, loc));
        }
    }
}