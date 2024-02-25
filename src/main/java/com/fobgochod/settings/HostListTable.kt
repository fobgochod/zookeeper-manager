package com.fobgochod.settings

import com.intellij.execution.util.ListTableWithButtons
import com.intellij.ide.ui.laf.darcula.DarculaUIUtil
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.util.*
import javax.swing.DefaultCellEditor
import javax.swing.table.TableCellEditor

/**
 *  multi host config
 *
 *  fun multiHost(): DialogPanel {
 *     return panel {
 *         row {
 *             val hostList = HostListTable()
 *             cell(hostList.component).verticalAlign(VerticalAlign.FILL)
 *                 .onIsModified { hostList.isModified() }
 *                 .onApply { hostList.apply() }
 *                 .onReset { hostList.reset() }
 *             panel { }
 *         }
 *     }
 * }
 *
 * @author fobgochod
 * @since 1.0
 */
class HostListTable : ListTableWithButtons<HostListTable.Item>() {

    data class Item(var host: String)

    companion object {
        private val state = ZKSettings.instance
        private val NAME_COLUMN = object : ColumnInfo<Item, String>("") {

            override fun valueOf(item: Item): String {
                return item.host
            }

            override fun getEditor(item: Item): TableCellEditor {
                val cellEditor = JBTextField()
                cellEditor.putClientProperty(DarculaUIUtil.COMPACT_PROPERTY, true)
                return DefaultCellEditor(cellEditor)
            }

            override fun isCellEditable(item: Item): Boolean {
                return true
            }

            override fun setValue(item: Item, value: String) {
                item.host = value
            }
        }
    }

    override fun createListModel(): ListTableModel<String> {
        return ListTableModel<String>(NAME_COLUMN)
    }

    override fun createElement(): Item {
        return Item("")
    }

    override fun isEmpty(element: Item): Boolean {
        return element.host.isEmpty()
    }

    override fun isUpDownSupported(): Boolean {
        return true
    }

    override fun cloneElement(variable: Item): Item {
        return Item(variable.host)
    }

    override fun canDeleteElement(selection: Item): Boolean {
        return true
    }

    fun reset() {
        val rows: MutableList<Item> = LinkedList()
        state.hostRows.forEach { rows.add(Item(it)) }
        setValues(rows)
    }

    fun apply() {
        state.hostRows.clear()
        elements.forEach { state.hostRows.add(it.host) }
    }

    fun isModified(): Boolean {
        val rows: MutableList<String> = LinkedList()
        elements.forEach { rows.add(it.host) }
        return state.hostRows != rows
    }
}
