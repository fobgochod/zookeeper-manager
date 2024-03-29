package com.fobgochod.view.action.popup;

import com.fobgochod.constant.ZKCli;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.util.StringUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.editor.ZKFileTypePanel;
import com.fobgochod.view.editor.ZKNodeEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.DocumentAdapter;
import org.apache.zookeeper.CreateMode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class CreateNodeUI {

    private final transient Project project;
    private JPanel root;
    private JTextField nodePath;
    private JComboBox<CreateMode> nodeMode;
    private JTextField nodeTtl;
    private JPanel dataPanel;
    private JLabel nodeModeTip;
    private ZKNodeEditor nodeData;

    public CreateNodeUI(@NotNull Project project) {
        this.project = project;
        root.setPreferredSize(ZKConstant.DIALOG_SIZE);

        initView();
        initEvent();
    }

    private void initView() {
        nodeMode.setModel(new DefaultComboBoxModel<>(CreateMode.values()));

        nodeData = new ZKNodeEditor(project);
        dataPanel.add(nodeData, BorderLayout.CENTER);

        ZKFileTypePanel zkFileTypePanel = new ZKFileTypePanel(nodeData);
        dataPanel.add(zkFileTypePanel, BorderLayout.SOUTH);
    }

    private void initEvent() {
        // 根据节点类型，控制TTL是否可编辑
        nodeMode.addItemListener(e -> {
            CreateMode item = (CreateMode) e.getItem();
            nodeTtl.setEditable(item.isTTL());
            if (item.isTTL()) {
                nodeModeTip.setVisible(true);
                nodeModeTip.setText("Config extendedTypesEnabled=true to enable the creation of TTL Nodes.");
            } else {
                nodeModeTip.setVisible(false);
                nodeModeTip.setText(null);
            }
        });

        // 非数字弹出提示
        new ComponentValidator(project).withValidator(() -> {
            if (isNumber()) {
                return null;
            }
            return new ValidationInfo(ZKBundle.message("please.enter.a.number"), nodeTtl);
        }).installOn(nodeTtl);

        nodeTtl.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(nodeTtl).ifPresent(ComponentValidator::revalidate);
            }
        });
    }

    private boolean isNumber() {
        try {
            Integer.parseInt(nodeTtl.getText());
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public JPanel getRoot() {
        return root;
    }

    public CreateMode getNodeMode() {
        return (CreateMode) nodeMode.getSelectedItem();
    }

    public JTextField getNodePath() {
        return nodePath;
    }

    public byte[] getNodeData() {
        return ZKCli.getBytes(nodeData.getText());
    }

    public long getTTL() {
        String text = nodeTtl.getText();
        return Long.parseLong(text);
    }

    public String getError() {
        if (StringUtil.isEmpty(nodePath.getText())) {
            return ZKBundle.message("please.enter.a.path");
        }
        CreateMode selectedItem = (CreateMode) nodeMode.getSelectedItem();
        if (selectedItem != null && selectedItem.isTTL()) {
            if (!isNumber()) {
                return ZKBundle.message("please.enter.a.number");
            }
        }
        return null;
    }
}
