package codr7.eli;

public record Pair(IValue left, IValue right) {
    public String dump(final VM vm) {
        return left.dump(vm) + ':' + right.dump(vm);
    }
}
