package codr7.jx.libs.core.types;

import codr7.jx.*;

public class JMacroType extends BaseType<JMacro> implements CallTrait {
    public JMacroType(final String id) { super(id); }

    @Override public void call(final VM vm,
                     final IValue target,
                     final int rArgs,
                     final int arity,
                     final int rResult,
                     final Loc loc) {
        throw new RuntimeException("Not implemented");
    }

    @Override public String dump(final VM vm, final IValue value) {
        return "(JMacro " + value.cast(this).id() + ")";
    }

    @Override public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Loc loc) {
        final IForm[] args = new IForm[body.length - 1];
        System.arraycopy(body, 1, args, 0, args.length);
        target.cast(this).emit(vm, args, rResult, loc);
    }
}
