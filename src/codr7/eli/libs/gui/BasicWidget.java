package codr7.eli.libs.gui;

import java.awt.*;

public class BasicWidget implements Widget {
    public final Component component;

    public BasicWidget(final Component imp) {
        this.component = imp;
    }

    @Override
    public Component component() {
        return component;
    }
}
