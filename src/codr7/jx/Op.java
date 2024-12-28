package codr7.jx;

import codr7.jx.ops.*;

import java.util.Set;

public record Op(OpCode code, Record data, Loc loc) {
    public String dump(final VM vm) {
        if (data == null) {
            return code.name();
        }

        return code.name() + switch (data) {
            case AddItem op -> " " + op.toString(vm);
            case Bench op -> " " + op.toString(vm);
            case Branch op -> " " + op.toString(vm);
            case CallRegister op -> " " + op.toString(vm);
            case CallValue op -> " " + op.toString(vm);
            case Check op -> " " + op.toString(vm);
            case Copy op -> " " + op.toString(vm);
            case CreateList op -> " " + op.toString(vm);
            case Dec op -> " " + op.toString(vm);
            case Goto op -> " " + op.toString(vm);
            case Left op -> " " + op.toString(vm);
            case Next op -> " " + op.toString(vm);
            case Put op -> " " + op.toString(vm);
            case Right op -> " " + op.toString(vm);
            case SetPath op -> " " + op.toString(vm);
            case Zip op -> " " + op.toString(vm);
            default -> "";
        };
    }

    public void io(final Set<Integer> read, final Set<Integer> write) {
        if (data == null) { return; }

        switch (data) {
            case AddItem op:
                op.io(read, write);
                break;
            case Bench op:
                op.io(read, write);
                break;
            case Branch op:
                op.io(read, write);
                break;
            case CallRegister op:
                op.io(read, write);
                break;
            case CallValue op:
                op.io(read, write);
                break;
            case Check op:
                op.io(read, write);
                break;
            case Copy op:
                op.io(read, write);
                break;
            case CreateList op:
                op.io(read, write);
                break;
            case Dec op:
                op.io(read, write);
                break;
            case Left op:
                op.io(read, write);
                break;
            case Next op:
                op.io(read, write);
                break;
            case Put op:
                op.io(read, write);
                break;
            case Right op:
                op.io(read, write);
                break;
            case Zip op:
                op.io(read, write);
                break;
            default:
                break;
        }
    }

    public Op relocate(final int deltaPc) {
        return switch (data) {
            case Bench op -> op.relocate(deltaPc, loc);
            case Branch op -> op.relocate(deltaPc, loc);
            case Goto op -> op.relocate(deltaPc, loc);
            default -> this;
        };
    }
}