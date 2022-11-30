package com.fobgochod.setting;

import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.setting.ZKConfigUI;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class ZKConfigConfigurable implements Configurable {

    private ZKConfigUI component;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return ZKBundle.message("setting.configurable.displayName");
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return component.getHost();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        component = new ZKConfigUI();
        return component.getRoot();
    }

    @Override
    public boolean isModified() {
        ZKConfigState config = ZKConfigState.getInstance();

        String name = component.getName().getText().trim();
        String host = component.getHost().getText().trim();
        int port = Integer.parseInt(component.getPort().getText().trim());
        String paths = component.getPaths().getText().trim();
        int blockUntilConnected = Integer.parseInt(component.getBlockUntilConnected().getText().trim());
        boolean saslClientEnabled = component.getSaslClientEnabled().isSelected();
        String username = component.getUsername().getText();
        String password = component.getPassword().getText();

        return !(name.equals(config.getName())
                && host.equals(config.getHost())
                && port == config.getPort()
                && paths.equals(config.getHost())
                && blockUntilConnected == config.getBlockUntilConnected()
                && saslClientEnabled == config.isSaslClientEnabled()
                && username.equals(config.getUsername())
                && password.equals(config.getPassword())
        );
    }

    @Override
    public void apply() {
        ZKConfigState config = ZKConfigState.getInstance();
        config.setName(component.getName().getText().trim());
        config.setHost(component.getHost().getText().trim());
        config.setPort(Integer.parseInt(component.getPort().getText().trim()));
        config.setPaths(component.getPaths().getText());
        config.setBlockUntilConnected(Integer.parseInt(component.getBlockUntilConnected().getText()));
        config.setSaslClientEnabled(component.getSaslClientEnabled().isSelected());
        config.setUsername(component.getUsername().getText());

        // 密码另外处理
        Credentials credentials = new Credentials("password", component.getPassword().getPassword());
        PasswordSafe.getInstance().set(ZKConfigState.credentialAttributes(), credentials);
    }

    @Override
    public void reset() {
        ZKConfigState config = ZKConfigState.getInstance();

        component.getName().setText(config.getName());
        component.getHost().setText(config.getHost());
        component.getPort().setText(String.valueOf(config.getPort()));
        component.getPaths().setText(config.getPaths());
        component.getBlockUntilConnected().setText(String.valueOf(config.getBlockUntilConnected()));
        component.getSaslClientEnabled().setSelected(config.isSaslClientEnabled());
        component.getUsername().setText(config.getUsername());
        component.getPassword().setText(PasswordSafe.getInstance().getPassword(ZKConfigState.credentialAttributes()));
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }

}
