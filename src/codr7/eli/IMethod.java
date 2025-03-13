package codr7.eli;

public interface IMethod extends Comparable<IMethod> {
    Arg[] args();

    int minArity();
    int maxArity();

    void call(VM vm,
              IValue[] args,
              int rResult,
              boolean eval,
              Loc loc);

    @Override
    default int compareTo(IMethod o) {
        var r = Integer.compare(minArity(), o.minArity());

        if (r != 0) {
            return r;
        }

        r = Integer.compare(maxArity(), o.maxArity());

        if (r != 0) {
            return r;
        }

        return Integer.compare(o.weight(), weight());
    }

    String dump(VM vm);
    String id();
    int weight();
}
