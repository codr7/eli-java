package codr7.jx.libs;

import codr7.jx.Arg;
import codr7.jx.Lib;
import codr7.jx.Value;
import codr7.jx.errors.EvalError;
import codr7.jx.libs.core.types.CallTrait;
import codr7.jx.libs.gui.shims.TabView;
import codr7.jx.libs.gui.shims.Table;
import codr7.jx.libs.gui.types.*;
import codr7.jx.libs.gui.shims.Container;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;

public class GUI extends Lib {
    public static final WidgetType widgetType = new WidgetType("Widget", Core.anyType);
    public static final ContainerType containerType = new ContainerType("Container", widgetType);

    public static final ButtonType buttonType = new ButtonType("Button", containerType);
    public static final ColumnType columnType = new ColumnType("Column", Core.anyType);
    public static final FrameType frameType = new FrameType("Frame", widgetType);
    public static final TabViewType tabViewType = new TabViewType("TabView", containerType);
    public static final TableType tableType = new TableType("TableView", containerType);

    public GUI() {
        super("gui");

        bind(buttonType);
        bind(columnType);
        bind(containerType);
        bind(frameType);
        bind(tabViewType);
        bind(tableType);
        bind(widgetType);

        bindMethod("add", new Arg[]{new Arg("parent", containerType), new Arg("child", widgetType)}, null,
                (vm, args, rResult, location) -> {
                    final var p = args[0].cast(containerType);
                    final var c = args[1].cast(widgetType);
                    p.container.add(c.component());
                });

        bindMethod("add-column",
                new Arg[]{new Arg("table", tableType), new Arg("title", Core.stringType)},
                columnType,
                (vm, args, rResult, location) -> {
                    final var table = args[0].cast(tableType).table();
                    final var title = args[1].cast(Core.stringType);
                    final var c = new TableColumn();
                    c.setHeaderValue(title);
                    table.addColumn(c);
                });

        bindMethod("add-tab",
                new Arg[]{new Arg("parent", tabViewType),
                        new Arg("title", Core.stringType),
                new Arg("child", widgetType)}, null,
                (vm, args, rResult, location) -> {
                    final var title = args[1].cast(Core.stringType);
                    final var child = args[2].cast(widgetType);
                    args[0].cast(tabViewType).tabbedPane.add(title, child.component());
                });

        bindMethod("button",
                new Arg[]{new Arg("title", Core.anyType), new Arg("on-click", Core.anyType)},
                buttonType,
                (vm, args, rResult, loc) -> {
                    final var title = args[0].cast(Core.stringType);
                    final var b = new codr7.jx.libs.gui.shims.Button(new JButton(title));
                    final var c = args[1];

                    if (c.type() instanceof CallTrait ct) {
                        b.button.addActionListener((_) -> ct.call(vm, c, args, rResult, loc));
                    } else {
                        throw new EvalError("Not callabale: " + c.dump(vm), loc);
                    }

                    vm.registers.set(rResult, new Value<>(buttonType, b));
                });

        bindMethod("column-layout", new Arg[]{new Arg("target", containerType)}, null,
                (vm, args, rResult, location) -> {
                    final var t = args[0].cast(containerType).container;
                    t.setLayout(new BoxLayout(t, BoxLayout.PAGE_AXIS));
                });

        bindMethod("content", new Arg[]{new Arg("frame", frameType)}, containerType,
                (vm, args, rResult, location) -> {
                    final var f = args[0].cast(frameType).frame;
                    vm.registers.set(rResult, new Value<>(containerType, new Container(f.getContentPane())));
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
                    vm.registers.set(rResult, new Value<>(frameType, new codr7.jx.libs.gui.shims.Frame(f)));
                });

        bindMethod("pack", new Arg[]{new Arg("frames*")}, null,
                (vm, args, rResult, location) -> {
                    for (final var a: args) { a.cast(frameType).frame.pack(); }
                });

        bindMethod("panel",
                new Arg[]{}, null,
                (vm, args, rResult, location) -> {
                    final var p = new JPanel();
                    p.setLayout(new BorderLayout());
                    vm.registers.set(rResult, new Value<>(containerType, new Container(p)));
                });

        bindMethod("row-layout", new Arg[]{new Arg("target", containerType)}, null,
                (vm, args, rResult, location) -> {
                    final var t = args[0].cast(containerType).container;
                    t.setLayout(new BoxLayout(t, BoxLayout.LINE_AXIS));
                });

        bindMethod("table", new Arg[]{}, tableType,
                (vm, args, rResult, location) -> {
                    final var t = new JTable();
                    t.setFillsViewportHeight(true);
                    t.setSelectionMode(SINGLE_INTERVAL_SELECTION);
                    JScrollPane s = new JScrollPane(t);
                    vm.registers.set(rResult, new Value<>(tableType, new Table(t, s)));
                });

        bindMethod("tab-view", new Arg[]{}, tabViewType,
                (vm, args, rResult, location) -> {
                    vm.registers.set(rResult, new Value<>(tabViewType, new TabView(new JTabbedPane())));
                });

        bindMethod("show", new Arg[]{new Arg("widgets*")}, null,
                (vm, args, rResult, location) -> {
                    for (final var a: args) { a.cast(widgetType).component().setVisible(true); }
                });
    }
}