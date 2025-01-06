package codr7.jx.libs.gui.shims;

import javax.swing.*;

public class TabView extends BasicWidget {
    public final JTabbedPane tabbedPane;

    public TabView(final JTabbedPane imp) {
        super(imp);
        this.tabbedPane = imp;
    }
}
