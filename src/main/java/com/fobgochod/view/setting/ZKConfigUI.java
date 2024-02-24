package com.fobgochod.view.setting;

import com.fobgochod.ZKClient;
import com.fobgochod.setting.ZKConfigState;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.messages.MessageDialog;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ZooKeeper application configurable
 *
 * @author fobgochod
 * @date 2022/10/17 23:47
 */
public class ZKConfigUI {

    private JPanel root;
    private JTextField name;
    private JTextField host;
    private JTextField port;
    private JTextField path;
    private JTextField blockUntilConnected;
    private JButton testConnectionButton;
    private JCheckBox saslClientEnabled;
    private JTextField username;
    private JPasswordField password;

    public ZKConfigUI() {
        testConnectionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String connectString = ZKConfigState.connectString(host.getText(), Integer.parseInt(port.getText()), path.getText());
                boolean zookeeper = ZKClient.getInstance().init(connectString, Integer.parseInt(blockUntilConnected.getText()), saslClientEnabled.isSelected());
                if (zookeeper) {
                    String title = "Connection to " + host.getText();
                    String content = "Successfully connected!";
                    MessageDialog messageDialog = new MessageDialog(content, title, new String[]{"OK"}, 0, AllIcons.General.BalloonInformation);
                    messageDialog.setSize(300, 120);
                    messageDialog.show();
                } else {
                    String title = "Connection to " + host.getText();
                    String content = "Cannot connect to remote host";
                    MessageDialog messageDialog = new MessageDialog(content, title, new String[]{"OK"}, 0, AllIcons.General.BalloonError);
                    messageDialog.setSize(300, 120);
                    messageDialog.show();
                }
                super.mouseClicked(e);
            }
        });
    }

    public static ZKConfigUI getInstance() {
        return ApplicationManager.getApplication().getService(ZKConfigUI.class);
    }

    public JPanel getRoot() {
        return root;
    }

    public JTextField getName() {
        return name;
    }

    public JTextField getHost() {
        return host;
    }

    public JTextField getPort() {
        return port;
    }

    public JTextField getPath() {
        return path;
    }

    public JTextField getBlockUntilConnected() {
        return blockUntilConnected;
    }

    public JCheckBox getSaslClientEnabled() {
        return saslClientEnabled;
    }

    public JTextField getUsername() {
        return username;
    }

    public JPasswordField getPassword() {
        return password;
    }
}
