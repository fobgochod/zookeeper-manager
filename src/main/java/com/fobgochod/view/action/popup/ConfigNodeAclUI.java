package com.fobgochod.view.action.popup;

import com.fobgochod.constant.AclScheme;
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
    private JPanel root;
    private JComboBox<String> schemeBox;
    private JTextField idField;
    private JCheckBox createBox;
    private JCheckBox deleteBox;
    private JCheckBox writeBox;
    private JCheckBox readBox;
    private JCheckBox adminBox;
    private JPanel permsPanel;

    public ConfigNodeAclUI(List<? extends ACL> aclList) {
        root.setPreferredSize(new Dimension(460, 200));

        idField.setText("anyone");
        idField.setEditable(false);
        aclList.forEach(data::addElement);

        schemeBox.setModel(new DefaultComboBoxModel<>(new String[]{
                AclScheme.world.key(),
                AclScheme.auth.key(),
                AclScheme.digest.key(),
                AclScheme.ip.key(),
                AclScheme.x509.key(),
        }));
        schemeBox.addItemListener(e -> {
            String item = (String) e.getItem();
            if (AclScheme.world.key().equals(item)) {
                idField.setText("anyone");
                idField.setEditable(false);
            } else {
                idField.setText("");
                idField.setEditable(true);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Remove Item");
        menuItem.setIcon(AllIcons.General.Remove);
        menuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                data.removeElement(perms.getSelectedValue());
            }
        });
        popupMenu.add(menuItem);
        perms.setComponentPopupMenu(popupMenu);

        permsPanel.add(
                ToolbarDecorator.createDecorator(perms)
                        .setAddAction(anActionButton -> {
                            ACL acl = newAcl();
                            if (!data.contains(acl)) {
                                data.addElement(acl);
                            }
                        }).setRemoveAction(anActionButton -> data.removeElement(perms.getSelectedValue())).createPanel(),
                BorderLayout.CENTER);
    }

    private ACL newAcl() {
        String schema = (String) schemeBox.getSelectedItem();
        String id = idField.getText();
        int perms = (createBox.isSelected() ? ZooDefs.Perms.CREATE : 0)
                | (deleteBox.isSelected() ? ZooDefs.Perms.DELETE : 0)
                | (writeBox.isSelected() ? ZooDefs.Perms.WRITE : 0)
                | (readBox.isSelected() ? ZooDefs.Perms.READ : 0)
                | (adminBox.isSelected() ? ZooDefs.Perms.ADMIN : 0);

        return new ZKAcl(perms, new Id(schema, id));
    }

    public JPanel getRoot() {
        return root;
    }

    public JComboBox<String> getScheme() {
        return schemeBox;
    }

    public List<ACL> getData() {
        return Arrays.stream(data.toArray()).map(p -> (ACL) p).collect(Collectors.toList());
    }
}
