package codr7.eli.libs.gui.shims;

import javax.swing.*;

public class Button extends BasicWidget {
    public final JButton button;

    public Button(final JButton imp) {
        super(imp);
        button = imp;
    }
}
