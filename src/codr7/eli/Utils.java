package codr7.eli;

public enum Utils {
    ;

    public static int log2(final long n) {
        return (int)(Math.log(n) / Math.log(2)) + 1;
    }
}
