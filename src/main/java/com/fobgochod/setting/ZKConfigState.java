package com.fobgochod.setting;

import com.fobgochod.util.StringUtil;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * SSL: Secure Sockets Layer 安全套接字协议
 * SASL: Simple Authentication and Security Layer
 * <p>
 * zookeeper configuration persistence
 *
 * @author Xiao
 * @date 2022/10/12 20:48
 */
@State(name = ZKConfigState.NAME, storages = @Storage(ZKConfigState.STORAGES))
public class ZKConfigState implements PersistentStateComponent<ZKConfigState> {

    public static final String NAME = "com.fobgochod.setting.ZkConfigState";
    public static final String STORAGES = "ZooKeeperSettingsPlugin.xml";

    private String name;
    private String host;
    private int port = 2181;
    private String paths;
    private int blockUntilConnected = 20 * 1000;
    private boolean saslClientEnabled;
    private String username;
    private String password;

    public static ZKConfigState getInstance() {
        return ApplicationManager.getApplication().getService(ZKConfigState.class);
    }

    public static String zkUrl(String host, int port) {
        if (host.contains(":")) {
            return host;
        } else if (host.contains(",")) {
            return host.replaceAll("[\\s,]+", ":" + port + ",");
        } else {
            return host + ":" + port;
        }
    }

    public static CredentialAttributes credentialAttributes() {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("ZooConfigState", "password"));
    }

    @Nullable
    public ZKConfigState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ZKConfigState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public int getBlockUntilConnected() {
        return blockUntilConnected;
    }

    public void setBlockUntilConnected(int blockUntilConnected) {
        this.blockUntilConnected = blockUntilConnected;
    }

    public boolean isSaslClientEnabled() {
        return saslClientEnabled;
    }

    public void setSaslClientEnabled(boolean saslClientEnabled) {
        this.saslClientEnabled = saslClientEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return PasswordSafe.getInstance().getPassword(credentialAttributes());
    }

    public String getZkUrl() {
        return zkUrl(host, port);
    }

    public String getTitle() {
        if (StringUtil.isNotEmpty(name)) {
            return name;
        }
        String zkUrl = getZkUrl();
        if (zkUrl.contains(",")) {
            return zkUrl.substring(0, zkUrl.indexOf(","));
        }
        return zkUrl;
    }
}
