package codr7.jx.libs.gui.types;

import codr7.jx.BaseType;
import codr7.jx.IType;

import javax.swing.table.TableColumn;

public class ColumnType extends BaseType<TableColumn> {
    public ColumnType(final String id, final IType...parentTypes) { super(id, parentTypes); }
}