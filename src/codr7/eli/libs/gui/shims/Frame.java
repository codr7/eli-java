package codr7.eli.libs.gui.shims;

import javax.swing.*;

public class Frame extends BasicWidget {
    public final JFrame frame;

    public Frame(final JFrame imp) {
        super(imp);
        frame = imp;
    }
}
