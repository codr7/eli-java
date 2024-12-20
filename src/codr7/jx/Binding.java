package codr7.jx;

public record Binding(IType type, int rValue) {
    public String toString() { return "#" + rValue + ":" + type.id(); }
}
