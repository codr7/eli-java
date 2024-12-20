package codr7.jx.libs.core.types;

import codr7.jx.*;

public class JMacroType extends BaseType<JMacro> implements CallTrait {
    public JMacroType(final String id) { super(id); }

    public void call(final VM vm,
                     final IValue target,
                     final int rArguments,
                     final int arity,
                     final int rResult,
                     final Location location) {
        throw new RuntimeException("Not implemented");
    }

    public void emitCall(final VM vm,
                         final IValue target,
                         final IForm[] body,
                         final int rResult,
                         final Location location) {
        final IForm[] args = new IForm[body.length - 1];
        System.arraycopy(body, 1, args, 0, args.length);
        target.cast(this).emit(vm, args, rResult, location);
    }
}
