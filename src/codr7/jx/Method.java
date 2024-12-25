package codr7.jx;

public record Method(String id, Arg[] args, int rArgs, IType resultType, int rResult, int startPc, int endPc) {
    public int arity() {
        var result = args.length;
        if (result > 0 && args[result-1].id().endsWith("*")) { return -1; }
        return result;
    }
}
