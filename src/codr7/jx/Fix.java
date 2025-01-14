package codr7.jx;

public final class Fix {
    public static final int EXP_BITS = 4;
    public static final int HEADER_BITS = EXP_BITS+1;

    private static final long[] scaleTable = new long[]
            {
                1,
                10L,
                100L,
                1000L,
                10000L,
                100000L,
                1000000L,
                10000000L,
                100000000L,
                1000000000L,
                10000000000L,
                100000000000L,
                1000000000000L,
                10000000000000L,
                100000000000000L
            };


    public static long add(final long lhs, final long rhs) {
       final var le = exp(lhs);
       final var re = exp(rhs);

       return (le == re)
               ? make(le, value(lhs) + value(rhs))
               : make(le, value(lhs) + value(rhs) * scale(le) / scale(re));
    }

    public static long div(final long lhs, final long rhs) {
        return make(exp(lhs), value(lhs) / value(rhs) / scale(exp(rhs)));
    }

    public static String dump(final long it, final boolean forceZero) {
        final var result = new StringBuilder();
        if (isNeg(it)) { result.append('-'); }
        final var t = it;
        if (t > 0 || forceZero) { result.append(t); }
        result.append('.');
        result.append(frac(it));
        return result.toString();
    }

    public static int exp(final long it) {
        return (int)it & ((1 << EXP_BITS) - 1);
    }

    public static long frac(final long it) {
        return value(it) % scale(exp(it));
    }

    public static boolean isNeg(final long it) {
        return ((it >> EXP_BITS) & 1) == 1;
    }

    public static long make(final int exp, final long value) {
        return (exp & ((1 << EXP_BITS) - 1)) +
                ((value < 0) ? (1 << EXP_BITS) : 0) +
                (Math.abs(value) << HEADER_BITS);
    }

    public static long mul(final long lhs, final long rhs) {
        return make(exp(lhs), value(lhs) + value(rhs) / scale(exp(rhs)));
    }

    public static long scale(final long exp) {
        return scaleTable[(int)exp];
    }

    public static long sub(final long lhs, final long rhs) {
        final var le = exp(lhs);
        final var re = exp(rhs);

        return (le == re)
                ? make(le, value(lhs) - value(rhs))
                : make(le, value(lhs) - value(rhs) * scale(le) / scale(re));
    }

    public static long trunc(final long it) {
        return value(it) / scale(exp(it));
    }

    public static long value(final long it) {
        final var v = it >> HEADER_BITS;
        return isNeg(it) ? -v : v;
    }
}
