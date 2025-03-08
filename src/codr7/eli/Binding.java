package codr7.eli;

public record Binding(IType type, int rValue) {
    public String toString() {
        return "#" + rValue + ":" + ((type == null) ? "?" : type.id());
    }
}
