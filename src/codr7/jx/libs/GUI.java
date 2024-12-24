package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import codr7.jx.Value;
import codr7.jx.libs.gui.types.FrameType;
import codr7.jx.libs.gui.types.WidgetType;

import javax.swing.*;
import java.awt.*;

public class GUI extends Lib {
    public static final WidgetType widgetType = new WidgetType("Widget");
    public static final FrameType frameType = new FrameType("Frame", widgetType);

    public GUI() {
        super("gui");

        bind(frameType);
        bind(widgetType);

        bindMethod("new-frame",
                new Arg[]{new Arg("title", Core.stringType), new Arg("size", Core.pairType)}, null,
                (vm, args, rResult, location) -> {
                    final var title = args[0].cast(Core.stringType);
                    final var f = new JFrame(title);
                    final var size = args[1].cast(Core.pairType);
                    final var width = size.left().cast(Core.intType).intValue();
                    final var height = size.right().cast(Core.intType).intValue();
                    f.setPreferredSize(new Dimension(width, height));
                    f.setLocationRelativeTo(null);
                    vm.registers.set(rResult, new Value<>(frameType, f));
                });

        bindMethod("pack", new Arg[]{new Arg("frames*")}, null,
                (vm, args, rResult, location) -> {
                    for (final var a: args) { a.cast(frameType).pack(); }
                });

        bindMethod("show", new Arg[]{new Arg("widgets*")}, null,
                (vm, args, rResult, location) -> {
                    for (final var a: args) { a.cast(widgetType).setVisible(true); }
                });
    }
}