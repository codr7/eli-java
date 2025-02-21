package codr7.eli.libs.gui.shims;

import javax.swing.*;

public class OpenDialog extends BasicWidget {
    public final JFileChooser fileChooser;

    public OpenDialog(final JFileChooser imp) {
        super(imp);
        fileChooser = imp;
    }
}
