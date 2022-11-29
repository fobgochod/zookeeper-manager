package com.fobgochod.view.action.popup;

import com.fobgochod.constant.AclScheme;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.domain.ZKAcl;
import com.intellij.icons.AllIcons;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigNodeAclUI {

    private final DefaultListModel<ACL> data = new DefaultListModel<>();
    private final JList<ACL> perms = new JBList<>(data);
    private List<? extends ACL> aclList;
    private JMenuItem removeMenuItem;

    private JPanel root;
    private JComboBox<AclScheme> schemeBox;
    private JTextField idField;
    private JCheckBox createBox;
    private JCheckBox deleteBox;
    private JCheckBox writeBox;
    private JCheckBox readBox;
    private JCheckBox adminBox;
    private JPanel permsPanel;

    public ConfigNodeAclUI(List<? extends ACL> aclList) {
        this.aclList = aclList;
        root.setPreferredSize(ZKConstant.DIALOG_SIZE);

        initView();
        initEvent();
    }

    private void initView() {
        schemeBox.setModel(new DefaultComboBoxModel<>(AclScheme.values()));

        aclList.forEach(data::addElement);

        // 右键菜单
        removeMenuItem = new JMenuItem("Remove", AllIcons.General.Remove);
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(removeMenuItem);
        perms.setComponentPopupMenu(popupMenu);
    }

    private void initEvent() {
        schemeBox.addItemListener(e -> {
            AclScheme item = (AclScheme) e.getItem();
            if (AclScheme.world == item) {
                idField.setText("anyone");
                idField.setEditable(false);
            } else {
                idField.setText("");
                idField.setEditable(true);
            }
        });

        removeMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.removeElement(perms.getSelectedValue());
            }
        });

        permsPanel.add(
                ToolbarDecorator.createDecorator(perms)
                        .setAddAction(anActionButton -> {
                            ACL acl = newAcl();
                            if (!data.contains(acl)) {
                                data.addElement(acl);
                            }
                        })
                        .setRemoveAction(anActionButton -> data.removeElement(perms.getSelectedValue()))
                        .createPanel(),
                BorderLayout.CENTER);
    }

    private ACL newAcl() {
        AclScheme schema = (AclScheme) schemeBox.getSelectedItem();
        String id = idField.getText();
        int perms = (createBox.isSelected() ? ZooDefs.Perms.CREATE : 0)
                | (deleteBox.isSelected() ? ZooDefs.Perms.DELETE : 0)
                | (writeBox.isSelected() ? ZooDefs.Perms.WRITE : 0)
                | (readBox.isSelected() ? ZooDefs.Perms.READ : 0)
                | (adminBox.isSelected() ? ZooDefs.Perms.ADMIN : 0);

        return new ZKAcl(perms, new Id(schema.key(), id));
    }

    public JPanel getRoot() {
        return root;
    }

    public JComboBox<AclScheme> getScheme() {
        return schemeBox;
    }

    public List<ACL> getData() {
        return Arrays.stream(data.toArray()).map(p -> (ACL) p).collect(Collectors.toList());
    }
}
