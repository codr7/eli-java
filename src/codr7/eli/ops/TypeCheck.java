package codr7.eli.ops;

import codr7.eli.IType;
import codr7.eli.Loc;
import codr7.eli.Op;
import codr7.eli.VM;

public record TypeCheck(int rTarget, IType type, Loc loc) implements Op {
    @Override
    public String dump(final VM vm) {
        return "TypeCheck rTarget: " + rTarget + " (" + vm.registers.get(rTarget).dump(vm) + ") " +
                "type: " + type.id();
    }

    @Override
    public void eval(final VM vm) {
        final var v = vm.registers.get(rTarget);
        v.typeCheck(vm, type, loc);
        vm.pc++;
    }
}