package codr7.eli.libs.gui.shims;

import javax.swing.*;
import java.awt.*;

public record Table(JTable table, JScrollPane view) implements Widget {
    @Override public Component component() { return view; }
}
