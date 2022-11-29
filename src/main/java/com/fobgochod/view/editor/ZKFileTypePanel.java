package com.fobgochod.view.editor;

import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeRenderer;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ZKFileTypePanel extends JPanel {

    private final transient Map<ZKNode, FileType> dataFileTypeCache = new HashMap<>();
    private final ZKNodeEditor nodeData;
    private transient ZKNode selectionNode;
    private JLabel fileTypeLabel;
    private ComboBox<FileType> dataFileType;

    public ZKFileTypePanel(ZKNodeEditor nodeData) {
        setLayout(new BorderLayout());
        this.nodeData = nodeData;

        initView();
        initEvent();
    }

    public void setSelectionNode(ZKNode selectionNode) {
        this.selectionNode = selectionNode;
        this.dataFileType.setSelectedItem(getCacheType());
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }

    public FileType getCacheType() {
        if (selectionNode == null) {
            return ZKNodeEditor.TEXT_FILE_TYPE;
        }
        return dataFileTypeCache.getOrDefault(selectionNode, ZKNodeEditor.TEXT_FILE_TYPE);
    }

    private void setCacheType(@NotNull FileType fileType) {
        if (selectionNode == null) {
            return;
        }
        dataFileTypeCache.put(selectionNode, fileType);
    }

    private void initView() {
        fileTypeLabel = new JBLabel(ZKBundle.message("zookeeper.tab.data.FileType"));
        add(fileTypeLabel, BorderLayout.WEST);

        dataFileType = new ComboBox<>(new FileType[]{
                ZKNodeEditor.TEXT_FILE_TYPE,
                ZKNodeEditor.JSON_FILE_TYPE,
                ZKNodeEditor.HTML_FILE_TYPE,
                ZKNodeEditor.XML_FILE_TYPE
        });
        dataFileType.setFocusable(false);
        dataFileType.setRenderer(new FileTypeRenderer());
        dataFileType.setSelectedItem(getCacheType());
        add(dataFileType, BorderLayout.CENTER);
        setBorder(JBUI.Borders.emptyLeft(3));
    }

    private void initEvent() {
        dataFileType.addItemListener(event -> {
            ItemSelectable selectable = event.getItemSelectable();
            if (selectable == null) {
                return;
            }
            Object[] selects = selectable.getSelectedObjects();
            if (selects == null || selects.length < 1) {
                return;
            }
            Object select = selects[0];
            if (select instanceof FileType) {
                FileType fileType = (FileType) select;
                this.nodeData.setFileType(fileType);
                setCacheType(fileType);
            }
        });
    }
}
