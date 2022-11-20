package com.fobgochod.view.action.popup;

import org.apache.zookeeper.CreateMode;

import javax.swing.*;

public class CreateNodeUI {

    private JPanel root;
    private JComboBox<CreateMode> nodeMode;
    private JTextField nodePath;
    private JTextField nodeData;
    private JTextField nodeTtl;

    public CreateNodeUI() {
        nodeMode.setModel(new DefaultComboBoxModel<>(CreateMode.values()));

        nodeMode.addItemListener(e -> {
            CreateMode item = (CreateMode) e.getItem();
            nodeTtl.setEditable(item.isTTL());
        });
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
