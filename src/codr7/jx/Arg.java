package codr7.jx;

public record Arg(String id, IType type) {
    public Arg(String id) { this(id, null); }
}
