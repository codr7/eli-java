package codr7.eli;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public enum Utils {
    ;

    public static int log2(final long n) {
        return (int) (Math.log(n) / Math.log(2)) + 1;
    }

    public static <T> Stream<List<T>> combine(final T[] in) {
        final long N = (long) Math.pow(2, in.length);

        return StreamSupport.stream(new Spliterators.AbstractSpliterator<>(N, Spliterator.SIZED) {
            int i = 1;

            @Override
            public boolean tryAdvance(final Consumer<? super List<T>> action) {
                if (i < N) {
                    final var out = new ArrayList<T>(Long.bitCount(i));

                    for (var bit = 0; bit < in.length; bit++) {
                        if ((i & (1 << bit)) != 0) {
                            out.add(in[bit]);
                        }
                    }

                    action.accept(out);
                    i++;
                    return true;
                }

                return false;
            }
        }, false);
    }
}
