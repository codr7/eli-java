package codr7.jx;

public class Label {
    public int pc;
    public Label(final int pc) { this.pc = pc; }
    public String toString() { return "@" + pc; }
}
