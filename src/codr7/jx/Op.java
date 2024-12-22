package codr7.jx;

import codr7.jx.ops.*;

public record Op(OpCode code, Record data, Location location) {
    public String toString(final VM vm) {
        if (data == null) { return code.name(); }
        return code.name() + switch (data) {
            case AddItem op -> " " + op.toString(vm);
            case Branch op -> " " + op.toString(vm);
            case CallRegister op -> " " + op.toString(vm);
            case CallValue op -> " " + op.toString(vm);
            case Check op -> " " + op.toString(vm);
            case Copy op -> " " + op.toString(vm);
            case CreateList op -> " " + op.toString(vm);
            case Goto op -> " " + op.toString(vm);
            case Left op -> " " + op.toString(vm);
            case Put op -> " " + op.toString(vm);
            case Right op -> " " + op.toString(vm);
            case SetPath op -> " " + op.toString(vm);
            case Zip op -> " " + op.toString(vm);
            default -> "";
        };
    }
}