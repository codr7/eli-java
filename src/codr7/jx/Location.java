package codr7.jx;

public class Location {
    public final String source;
    private int line, column;

    public Location(final String source) {
        this(source, 1, 1);
    }

    private Location(final String source, final int line, final int column) {
        this.source = source;
        this.line = line;
        this.column = column;
    }

    public Location copy() {
        return new Location(source, line, column);
    }

    public char update(final char c) {
        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return c;
    }

    public String toString() {
        return String.format("%s@%d:%d", source, line, column);
    }
}
