package codr7.eli;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class BaseMethod implements IMethod {
    public final String id;
    public final Arg[] args;
    public final int minArity;
    public final int maxArity;
    public final int weight;

    public BaseMethod(final String id, final Arg[] args) {
        this.id = id;
        this.args = args;
        this.minArity = Arg.minArity(args);
        this.maxArity = Arg.maxArity(args);
        this.weight = Arg.weight(args);
    }

    @Override
    public Arg[] args() {
        return args;
    }

    @Override
    public int compareTo(final IMethod m) {
        return Integer.compare(m.weight(), weight);
    }

    @Override
    public String dump(final VM vm) {
        return "(^" + id + " [" +
                Arrays.stream(args).map(Arg::dump).collect(Collectors.joining(" ")) +
                "])";
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int minArity() {
        return minArity;
    }

    @Override
    public int maxArity() {
        return maxArity;
    }

    @Override
    public int weight() {
        return weight;
    }
}