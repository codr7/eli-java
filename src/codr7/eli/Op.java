package codr7.eli;

public interface Op {
    String dump(VM vm);
    void eval(VM vm);
}