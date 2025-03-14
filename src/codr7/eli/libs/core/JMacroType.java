package codr7.eli.libs.core;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.forms.LiteralForm;
import codr7.eli.forms.SplatForm;
import codr7.eli.libs.CoreLib;

import java.util.ArrayList;

public final class JMacroType extends BaseType<JMacro> implements CallableTrait {
    public JMacroType(final String id, final IType... parentTypes) {
        super(id, parentTypes);
    }

    @Override
    public void call(final VM vm,
                     final IValue target,
                     final IValue[] args,
                     final int rResult,
                     final boolean eval,
                     final Loc loc) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String dump(final VM vm, final IValue value) {
        return "(JMacro " + value.cast(this).id() + ")";
    }

    @Override
    public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Loc loc) {
        final var args = new ArrayList<IForm>();
        final var rIt = vm.alloc(1);

        for (var i = 1; i < body.length; i++) {
            final var f = body[i];

            if (f instanceof SplatForm sf) {
                final var t = sf.target.value(vm);
                if (t == null) {
                    throw new EmitError("Invalid splat: " + sf.target.dump(vm), loc);
                }
                final var it = ((IterableTrait) t.type()).iter(vm, t);

                while (it.next(vm, rIt, sf.target.loc())) {
                    final var v = vm.registers.get(rIt);

                    if (v.type() == CoreLib.Expr) {
                        args.add(v.cast(CoreLib.Expr));
                    } else {
                        args.add(new LiteralForm(v, loc));
                    }
                }
            } else {
                args.add(f);
            }
        }

        target.cast(this).emit(vm, args.toArray(new IForm[0]), rResult, loc);
    }
}
