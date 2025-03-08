package codr7.eli;

public interface Iter {
    String dump(VM vm);

    boolean next(VM vm, int rResult, Loc loc);
}
