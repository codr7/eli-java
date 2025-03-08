package codr7.eli;

public final class Loc {
    public final String source;
    private int line, column;

    private Loc(final String source, final int line, final int column) {
        this.source = source;
        this.line = line;
        this.column = column;
    }

    public Loc(final String source) {
        this(source, 1, 1);
    }

    public Loc dup() {
        return new Loc(source, line, column);
    }

    public int line() {
        return line;
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
