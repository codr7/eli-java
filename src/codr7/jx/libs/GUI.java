package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import codr7.jx.Value;
import codr7.jx.libs.gui.types.ContainerType;
import codr7.jx.libs.gui.types.FrameType;
import codr7.jx.libs.gui.types.TabViewType;
import codr7.jx.libs.gui.types.WidgetType;

import javax.swing.*;
import java.awt.*;

public class GUI extends Lib {
    public static final WidgetType widgetType = new WidgetType("Widget");
    public static final ContainerType containerType = new ContainerType("Container", widgetType);
    public static final FrameType frameType = new FrameType("Frame", widgetType);
    public static final TabViewType tabViewType = new TabViewType("TabView", containerType);

    public GUI() {
        super("gui");

        bind(containerType);
        bind(frameType);
        bind(tabViewType);
        bind(widgetType);

        bindMethod("add", new Arg[]{new Arg("parent", containerType), new Arg("child", widgetType)}, null,
                (vm, args, rResult, location) -> {
                    final var p = args[0].cast(containerType);
                    final var w = args[1].cast(widgetType);
                    p.add(w);
                });

        bindMethod("add-tab",
                new Arg[]{new Arg("parent", tabViewType),
                        new Arg("title", Core.stringType),
                new Arg("child", widgetType)}, null,
                (vm, args, rResult, location) -> {
                    final var title = args[1].cast(Core.stringType);
                    final var child = args[2].cast(widgetType);
                    args[0].cast(tabViewType).add(title, child);
                });

        bindMethod("box-layout", new Arg[]{new Arg("target", containerType)}, null,
                (vm, args, rResult, location) -> {
                    final var t = args[0].cast(containerType);
                    t.setLayout(new BoxLayout(t, BoxLayout.PAGE_AXIS));
                });

        bindMethod("content", new Arg[]{new Arg("frame", frameType)}, containerType,
                (vm, args, rResult, location) -> {
                    final var f = args[0].cast(frameType);
                    vm.registers.set(rResult, new Value<>(containerType, f.getContentPane()));
                });

        bindMethod("frame",
                new Arg[]{new Arg("title", Core.stringType), new Arg("size", Core.pairType)}, frameType,
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

        bindMethod("panel",
                new Arg[]{}, null,
                (vm, args, rResult, location) -> {
                    final var p = new JPanel();
                    p.setLayout(new BorderLayout());
                    vm.registers.set(rResult, new Value<>(containerType, p));
                });

        bindMethod("tabs", new Arg[]{}, containerType,
                (vm, args, rResult, location) -> {
                    vm.registers.set(rResult, new Value<>(widgetType, new JTabbedPane()));
                });

        bindMethod("show", new Arg[]{new Arg("widgets*")}, null,
                (vm, args, rResult, location) -> {
                    for (final var a: args) { a.cast(widgetType).setVisible(true); }
                });
    }
}