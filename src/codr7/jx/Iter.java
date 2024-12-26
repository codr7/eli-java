package codr7.jx;

public interface Iter {
    String dump(VM vm);
    boolean next(VM vm, int rResult, Loc loc);
}
