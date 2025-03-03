package codr7.eli.forms;

import codr7.eli.*;
import codr7.eli.errors.EmitError;
import codr7.eli.libs.core.traits.CallTrait;
import codr7.eli.ops.Left;
import codr7.eli.ops.Right;

public class CallForm extends BaseForm {
    private final IForm[] body;

    public CallForm(IForm[] body, final Loc loc) {
        super(loc);
        this.body = body;
    }

    @Override public void emit(final VM vm, final int rResult) {
        var getLeft = false;
        var rightCount = 0;
        var tf = body[0];

        while (tf instanceof PairForm pf) {
            if (pf.left.isNil()) {
                tf = pf.right;
                rightCount++;
                continue;
            }

            if (pf.right.isNil()) {
                getLeft = true;
                tf = pf.left;
                break;
            }

            throw new EmitError("Invalid target: %s" + tf, loc());
        }

        var t = tf.rawValue(vm);
        if (t.type() instanceof CallTrait ct) { ct.emitCall(vm, t, body, rResult, loc()); }
        else { throw new EmitError("Not callable: " + t.dump(vm), loc()); }
        if (getLeft) { vm.emit(new Left(rResult, rResult, tf.loc())); }
        else for (; rightCount > 0; rightCount--) { vm.emit(new Right(rResult, rResult, tf.loc())); }
    }

    @Override public String dump(VM vm) {
        final var result = new StringBuilder();
        result.append('(');

        for (var i = 0; i < body.length; i++) {
            if (i > 0) { result.append(' '); }
            result.append(body[i].dump(vm));
        }

        result.append(')');
        return result.toString();
    }

    @Override public boolean eq(final IForm other) {
        if (other instanceof CallForm f) {
            if (f.body.length != body.length) { return false; }

            for (var i = 0; i < body.length; i++) {
                if (!f.body[i].eq(body[i])) { return false; }
            }

            return true;
        }

        return false;
    }

    @Override public IValue rawValue(VM vm) {
        return eval(vm);
    }
}