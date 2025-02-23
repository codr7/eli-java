package codr7.eli.libs;

import codr7.eli.Arg;
import codr7.eli.Lib;
import codr7.eli.Value;
import codr7.eli.errors.EvalError;
import codr7.eli.libs.core.traits.CallTrait;
import codr7.eli.libs.gui.shims.OpenDialog;
import codr7.eli.libs.gui.shims.TabView;
import codr7.eli.libs.gui.shims.Table;
import codr7.eli.libs.gui.types.*;
import codr7.eli.libs.gui.shims.Container;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import java.awt.*;

import static javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION;

public class GUILib extends Lib {
    public static final WidgetType widgetType = new WidgetType("Widget", CoreLib.anyType);
    public static final ContainerType containerType = new ContainerType("Container", widgetType);

    public static final ButtonType buttonType = new ButtonType("Button", containerType);
    public static final ColumnType columnType = new ColumnType("Column", CoreLib.anyType);
    public static final FrameType frameType = new FrameType("Frame", widgetType);
    public static final OpenDialogType openDialogType = new OpenDialogType("OpenDialog", containerType);
    public static final TabViewType tabViewType = new TabViewType("TabView", containerType);
    public static final TableType tableType = new TableType("TableView", containerType);

    public GUILib() {
        super("gui");

        bind(buttonType);
        bind(columnType);
        bind(containerType);
        bind(frameType);
        bind(openDialogType);
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
                new Arg[]{new Arg("table", tableType), new Arg("title", CoreLib.stringType)},
                columnType,
                (vm, args, rResult, location) -> {
                    final var table = args[0].cast(tableType).table();
                    final var title = args[1].cast(CoreLib.stringType);
                    final var c = new TableColumn();
                    c.setHeaderValue(title);
                    table.addColumn(c);
                });

        bindMethod("add-tab",
                new Arg[]{new Arg("parent", tabViewType),
                        new Arg("title", CoreLib.stringType),
                new Arg("child", widgetType)}, null,
                (vm, args, rResult, location) -> {
                    final var title = args[1].cast(CoreLib.stringType);
                    final var child = args[2].cast(widgetType);
                    args[0].cast(tabViewType).tabbedPane.add(title, child.component());
                });

        bindMethod("button",
                new Arg[]{new Arg("title", CoreLib.anyType), new Arg("on-click", CoreLib.anyType)},
                buttonType,
                (vm, args, rResult, loc) -> {
                    final var title = args[0].cast(CoreLib.stringType);
                    final var b = new codr7.eli.libs.gui.shims.Button(new JButton(title));
                    final var c = args[1];

                    if (c.type() instanceof CallTrait ct) {
                        b.button.addActionListener((_) -> ct.call(vm, c, args, vm.alloc(1), loc));
                    } else {
                        throw new EvalError("Not callable: " + c.dump(vm), loc);
                    }

                    vm.registers.set(rResult, new Value<>(buttonType, b));
                });

        bindMethod("column-layout", new Arg[]{new Arg("target", containerType)}, null,
                (vm, args, rResult, loc) -> {
                    final var t = args[0].cast(containerType).container;
                    t.setLayout(new BoxLayout(t, BoxLayout.PAGE_AXIS));
                });

        bindMethod("content", new Arg[]{new Arg("frame", frameType)}, containerType,
                (vm, args, rResult, loc) -> {
                    final var f = args[0].cast(frameType).frame;
                    vm.registers.set(rResult, new Value<>(containerType, new Container(f.getContentPane())));
                });

        bindMethod("frame",
                new Arg[]{new Arg("title", CoreLib.stringType), new Arg("size", CoreLib.pairType)}, frameType,
                (vm, args, rResult, loc) -> {
                    final var title = args[0].cast(CoreLib.stringType);
                    final var f = new JFrame(title);
                    final var size = args[1].cast(CoreLib.pairType);
                    final var width = size.left().cast(CoreLib.intType).intValue();
                    final var height = size.right().cast(CoreLib.intType).intValue();
                    f.setPreferredSize(new Dimension(width, height));
                    f.setLocationRelativeTo(null);
                    vm.registers.set(rResult, new Value<>(frameType, new codr7.eli.libs.gui.shims.Frame(f)));
                });

        bindMethod("open-file",
                new Arg[]{new Arg("parent", widgetType), new Arg("filters", CoreLib.listType)},
                CoreLib.maybeType,
                (vm, args, rResult, loc) -> {
                    final var d = new OpenDialog(new JFileChooser());

                    if (args.length > 1) {
                        for (final var f: args[1].cast(CoreLib.listType)) {
                            final var fp = f.cast(CoreLib.pairType);
                            final var ext = fp.left().cast(CoreLib.symType);
                            final var inf = fp.right().cast(CoreLib.stringType);
                            d.fileChooser.setFileFilter(new FileNameExtensionFilter(inf, ext));
                        }
                    }

                    final var parent = args[0].cast(widgetType);

                    if (d.fileChooser.showOpenDialog(parent.component()) == JFileChooser.APPROVE_OPTION) {
                        vm.registers.set(rResult,
                                new Value<>(CoreLib.stringType,
                                        d.fileChooser.getSelectedFile().getName()));
                    } else {
                        vm.registers.set(rResult, CoreLib.NIL);
                    }
                });

        bindMethod("pack", new Arg[]{new Arg("frames*")}, null,
                (vm, args, rResult, loc) -> {
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
                (vm, args, rResult, loc) -> {
                    final var t = args[0].cast(containerType).container;
                    t.setLayout(new BoxLayout(t, BoxLayout.LINE_AXIS));
                });

        bindMethod("table", new Arg[]{}, tableType,
                (vm, args, rResult, loc) -> {
                    final var t = new JTable();
                    t.setFillsViewportHeight(true);
                    t.setSelectionMode(SINGLE_INTERVAL_SELECTION);
                    JScrollPane s = new JScrollPane(t);
                    vm.registers.set(rResult, new Value<>(tableType, new Table(t, s)));
                });

        bindMethod("tab-view", new Arg[]{}, tabViewType,
                (vm, args, rResult, loc) -> {
                    vm.registers.set(rResult, new Value<>(tabViewType, new TabView(new JTabbedPane())));
                });

        bindMethod("show", new Arg[]{new Arg("widgets*")}, null,
                (vm, args, rResult, loc) -> {
                    for (final var a: args) { a.cast(widgetType).component().setVisible(true); }
                });
    }
}