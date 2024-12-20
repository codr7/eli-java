package codr7.jx;

public record Pair(IValue left, IValue right) {
    public String dump(final VM vm) { return left.dump(vm) + ':' + right.dump(vm); }
}
