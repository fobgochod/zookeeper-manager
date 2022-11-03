package com.fobgochod.view.action.navigator;

import com.fobgochod.constant.AclScheme;

import javax.swing.*;

public class AclSchemeUI {

    private JPanel root;
    private JTextField username;
    private JPasswordField password;
    private JTable authInfo;
    private JComboBox<String> schemeBox;

    public AclSchemeUI() {
        schemeBox.setModel(new DefaultComboBoxModel<>(new String[]{
                AclScheme.digest.key()
        }));
    }

    public JPanel getRoot() {
        return root;
    }

    public String getScheme() {
        return schemeBox.getSelectedItem().toString();
    }

    public String getAuth() {
        return getUsername() + ":" + getPassword();
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public JTable getAuthInfo() {
        return authInfo;
    }
}
