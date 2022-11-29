package com.fobgochod.view.action.popup;

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
    private ZKNodeEditor nodeData;
    private ZKFileTypePanel zkFileTypePanel;

    public CreateNodeUI(@NotNull Project project) {
        this.project = project;
        root.setPreferredSize(new Dimension(460, 200));

        initView();
        initEvent();
    }

    private void initView() {
        nodeMode.setModel(new DefaultComboBoxModel<>(CreateMode.values()));

        nodeData = new ZKNodeEditor(project);
        dataPanel.add(nodeData, BorderLayout.CENTER);

        zkFileTypePanel = new ZKFileTypePanel(nodeData);
        dataPanel.add(zkFileTypePanel, BorderLayout.SOUTH);
    }

    private void initEvent() {
        // 根据节点类型，控制TTL是否可编辑
        nodeMode.addItemListener(e -> {
            CreateMode item = (CreateMode) e.getItem();
            nodeTtl.setEditable(item.isTTL());
        });

        // 控制非数字，无法转移焦点
        nodeTtl.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return isNumber();
            }

            @Override
            public boolean shouldYieldFocus(JComponent source, JComponent target) {
                return isNumber();
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
                ComponentValidator.getInstance(nodeTtl).ifPresent(v -> v.revalidate());
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
        return nodeData.getText().getBytes();
    }

    public long getTTL() {
        String text = nodeTtl.getText();
        return Long.parseLong(text);
    }
}
