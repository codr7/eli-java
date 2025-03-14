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

    String dump(VM vm);

    String id();

    int weight();
}
