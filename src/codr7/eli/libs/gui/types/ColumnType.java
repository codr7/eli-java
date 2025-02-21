package codr7.eli.libs.gui.types;

import codr7.eli.BaseType;
import codr7.eli.IType;

import javax.swing.table.TableColumn;

public class ColumnType extends BaseType<TableColumn> {
    public ColumnType(final String id, final IType...parentTypes) { super(id, parentTypes); }
}