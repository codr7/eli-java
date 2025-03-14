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
    default int compareTo(final IMethod m) {
        return Integer.compare(m.weight(), weight());
    }

    String dump(VM vm);
    String id();
    int weight();
}
