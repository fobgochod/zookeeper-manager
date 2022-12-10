package com.fobgochod.setting;

import com.fobgochod.constant.ZKConstant;
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
 * @author fobgochod
 * @date 2022/10/12 20:48
 */
@State(name = ZKConfigState.NAME, storages = @Storage(ZKConfigState.STORAGES))
public class ZKConfigState implements PersistentStateComponent<ZKConfigState> {

    public static final String NAME = "com.fobgochod.setting.ZkConfigState";
    public static final String STORAGES = "ZooKeeperSettingsPlugin.xml";

    private String name;
    private String host = "127.0.0.1";
    private int port = 2181;
    private String path;
    private int blockUntilConnected = 6 * 1000;
    private boolean saslClientEnabled;
    private String username;
    private String password;

    public static ZKConfigState getInstance() {
        return ApplicationManager.getApplication().getService(ZKConfigState.class);
    }

    /**
     * example1:
     * input:  host=192.168.10.10:2181  port=
     * output: 192.168.10.10:2181
     * <p>
     * example2:
     * input:  host=192.168.10.10,192.168.10.11,192.168.10.10  port=2181
     * output: 192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181
     * <p>
     * example3:
     * input:  host=192.168.10.10  port=2181
     * output: 192.168.10.10:2181
     *
     * @return connectString
     */
    public static String connectString(String host, int port, String path) {
        String address;
        if (host.contains(":")) {
            address = host;
        } else if (host.contains(",")) {
            address = host.replaceAll("[\\s,]+", ":" + port + ",");
        } else {
            address = host + ":" + port;
        }
        return address  + StringUtil.getPath(path);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String connectString() {
        return connectString(host, port, path);
    }

    /**
     * 根据连接字符串获取一个name
     * <p>
     * input:  192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181/hello
     * output: 192.168.10.10:2181/hello
     */
    public String getTitle() {
        if (StringUtil.isNotEmpty(name)) {
            return name;
        }
        String connectString = connectString();
        if (connectString.contains(",")) {
            return connectString.substring(0, connectString.indexOf(",")) + connectString.substring(connectString.indexOf(ZKConstant.SLASH));
        }
        return connectString;
    }
}
