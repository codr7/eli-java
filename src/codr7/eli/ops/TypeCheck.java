package codr7.eli.ops;

import codr7.eli.IType;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

import java.util.Set;

public record TypeCheck(int rTarget, IType type, Loc loc) implements Op {
    @Override
    public Code code() {
        return Code.TypeCheck;
    }

    @Override
    public String dump(final VM vm) {
        return "TypeCheck rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "type: " + type.id();
    }
}