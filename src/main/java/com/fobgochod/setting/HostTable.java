package com.fobgochod.setting;


import com.fobgochod.settings.ZKSettings;
import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.openapi.util.NlsContexts.ColumnName;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class HostTable extends ListTableWithButtons<String> {

    private static final ZKSettings state = ZKSettings.getInstance();

    @Override
    protected ListTableModel<String> createListModel() {
        return new ListTableModel<>(new NameColumnInfo(""));
    }

    @Override
    protected String createElement() {
        return "";
    }

    @Override
    protected boolean isEmpty(String element) {
        return element.isEmpty();
    }

    @Override
    protected String cloneElement(String element) {
        return element;
    }

    @Override
    protected boolean canDeleteElement(String element) {
        return true;
    }

    public void reset() {
        setValues(state.getHostRows());
    }

    public void apply() {
        state.getHostRows().clear();
        state.setHostRows(getElements());
    }

    public boolean isModified() {
        return state.getHostRows().equals(getElements());
    }

    protected static class NameColumnInfo extends ColumnInfo<String, String> {

        public NameColumnInfo(@ColumnName String name) {
            super(name);
        }

        @Override
        public @Nullable String valueOf(String item) {
            return item;
        }

        public @NotNull TableCellEditor getEditor(String item) {
            return new DefaultCellEditor(new JTextField());
        }

        @Override
        public boolean isCellEditable(String s) {
            return true;
        }
    }
}
