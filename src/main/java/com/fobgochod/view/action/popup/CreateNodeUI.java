package com.fobgochod.view.action.popup;

import org.apache.zookeeper.CreateMode;

import javax.swing.*;

public class CreateNodeUI {

    private JPanel root;
    private JComboBox<CreateMode> nodeMode;
    private JTextField nodePath;
    private JTextField nodeData;

    public CreateNodeUI() {
        nodeMode.setModel(new DefaultComboBoxModel<>(new CreateMode[]{
                CreateMode.PERSISTENT,
                CreateMode.PERSISTENT_SEQUENTIAL,
                CreateMode.EPHEMERAL,
                CreateMode.EPHEMERAL_SEQUENTIAL
        }));
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
}
