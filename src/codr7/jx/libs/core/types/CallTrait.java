package codr7.jx.libs.core.types;

import codr7.jx.IForm;
import codr7.jx.IValue;
import codr7.jx.Loc;
import codr7.jx.VM;

public interface CallTrait {
    void call(VM vm, IValue target, int rArguments, int arity, int rResult, Loc loc);
    void emitCall(VM vm, IValue target, IForm[] body, int rResult, Loc loc);
}
