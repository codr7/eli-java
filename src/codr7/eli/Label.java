package codr7.eli;

public final class Label {
    public int pc;
    public Label(final int pc) { this.pc = pc; }
    public String toString() { return "@" + pc; }
}
