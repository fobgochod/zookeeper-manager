package com.fobgochod;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.ClientInfo;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public interface ZKClient extends Disposable {

    static ZKClient getInstance() {
        return ApplicationManager.getApplication().getService(ZooClient.class);
    }

    @Override
    default void dispose() {
        this.close();
    }

    void close();

    boolean isConnected();

    boolean init(String connectString, int sessionTimeout, boolean enableClientSasl);

    void addAuthInfo(String scheme, String auth);

    List<ClientInfo> getClientInfo();

    void create(String path);

    void create(String path, byte[] data);

    void create(String path, byte[] data, CreateMode mode);

    void create(String path, byte[] data, CreateMode mode, long ttl);

    void delete(String path);

    void deleteChildren(String path);

    void setData(String path, byte[] data);

    byte[] getData(String path);

    byte[] getData(String path, Stat stat);

    Stat exists(String path);

    List<String> getChildren(String path);

    List<ACL> getACL(String path);

    void setACL(String path, List<ACL> acl);
}
