package codr7.jx;

import codr7.jx.errors.EmitError;
import codr7.jx.forms.QuoteForm;
import codr7.jx.libs.CoreLib;
import codr7.jx.ops.*;

import java.util.ArrayList;
import java.util.List;

public interface IForm {
    default String argId(final VM vm, final Loc loc) {
        throw new EmitError("Invalid arg: " + dump(vm), loc);
    }

    default void bind(final VM vm, final int rValue, final Loc loc) {
        throw new EmitError("Invalid bind target: " + dump(vm), loc);
    }

    void emit(VM vm, int rResult);

    boolean eq(IForm other);

    default IValue eval(final VM vm) {
        final var v = value(vm);
        if (v != null) { return v; }
        final var rResult = vm.alloc(1);
        final var skipPc = vm.emit(new Nop());
        final var start = vm.label();
        emit(vm, rResult);
        vm.ops.set(skipPc, new Goto(vm.label()));
        vm.eval(start.pc);
        return vm.registers.get(rResult);
    }

    default IType getType(final VM vm, final Loc loc) {
        if (isNil()) { return null; }
        final var v = value(vm);
        if (v == null) { throw new EmitError("Expected type: " + dump(vm), loc); }
        final var t = v.cast(CoreLib.metaType);
        if (t == null) { throw new EmitError("Expected type: " + dump(vm), loc); }
        return t;
    }

    default boolean isNil() { return false; }
    Loc loc();
    String dump(VM vm);

    default IValue quote(final VM vm, final Loc loc) {
        return new Value<>(CoreLib.exprType, new QuoteForm(this, loc));
    }

    IValue rawValue(VM vm);

    default void unquote(VM vm, int rResult, Loc loc) {
        var v = rawValue(vm);
        if (v == null) { v = eval(vm); }
        v.unquote(vm, rResult, loc);
    }

    default IValue value(final VM vm) {
        final var v = rawValue(vm);
        return (v == null || v.type() == CoreLib.bindingType) ? null : v;
    }
}
