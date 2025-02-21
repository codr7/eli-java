package codr7.eli.libs.gui.shims;

public class Container extends BasicWidget {
    public final java.awt.Container container;

    public Container(final java.awt.Container imp) {
        super(imp);
        this.container = imp;
    }
}
