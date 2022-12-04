package com.fobgochod;

import com.fobgochod.constant.ZKCli;
import com.fobgochod.setting.ZKConfigState;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.view.vfs.ZKNodeFile;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.Strings;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.ClientInfo;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZKClient implements Disposable {

    private CuratorFramework curator;

    public static ZKClient getInstance() {
        return ApplicationManager.getApplication().getService(ZKClient.class);
    }

    public boolean isConnected() {
        return curator != null && curator.getZookeeperClient().isConnected();
    }

    public void close() {
        if (curator != null) {
            // 关闭旧连接
            curator.close();
        }
    }

    public boolean initZookeeper() {
        ZKConfigState config = ZKConfigState.getInstance();
        return initZookeeper(config.getZkUrl(), config.getBlockUntilConnected(), config.isSaslClientEnabled());
    }

    public boolean initZookeeper(String connectString, int maxWaitTime, boolean saslClientEnabled) {
        if (isConnected() && curator.getZookeeperClient().getCurrentConnectionString().equals(connectString)) {
            return true;
        }
        try {
            close();
            System.setProperty(ZKClientConfig.ENABLE_CLIENT_SASL_KEY, String.valueOf(saslClientEnabled));
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            curator = CuratorFrameworkFactory.builder().connectString(connectString).retryPolicy(retryPolicy).build();
            curator.start();
            boolean connected = curator.blockUntilConnected(maxWaitTime, TimeUnit.MILLISECONDS);
            if (connected) {
                NoticeUtil.debug(connectString + " successfully connected!");
                return true;
            }
        } catch (Exception e) {
            NoticeUtil.notify("cannot connect to remote host " + connectString + " :" + e.getMessage(), NotificationType.ERROR);
        }
        close();
        NoticeUtil.warn(connectString + " connection failed!");
        return false;
    }

    public void update(ZKNodeFile virtualFile, String data) {
        if (virtualFile.isSingleFileZip()) {
            this.setData(virtualFile.getMyPath(), ZKNodeFile.zip(virtualFile.getName().replace(".zip", ""), data.getBytes()));
        } else {
            this.setData(virtualFile.getMyPath(), data.getBytes());
        }
    }

    @Override
    public void dispose() {
        this.close();
    }

    public void addAuthInfo(String scheme, String auth) {
        try {
            curator.getZookeeperClient().getZooKeeper().addAuthInfo(scheme, auth.getBytes());
            NoticeUtil.debug(ZKCli.getLog(ZKCli.addauth, scheme, auth));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.addauth, scheme, auth), e.getMessage());
        }
    }

    public List<ClientInfo> getClientInfo() {
        try {
            List<ClientInfo> clientInfos = curator.getZookeeperClient().getZooKeeper().whoAmI();
            return clientInfos == null ? new ArrayList<>() : clientInfos;
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.whoami), e.getMessage());
        }
        return new ArrayList<>();
    }

    public void create(String path) {
        try {
            curator.create().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create, path), e.getMessage());
        }
    }

    public void create(String path, byte[] data) {
        try {
            curator.create().forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_data, path, ZKCli.data(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_data, path, ZKCli.data(data)), e.getMessage());
        }
    }

    public void creatingParentsIfNeeded(String path, byte[] data, CreateMode mode) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.data(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.data(data)), e.getMessage());
        }
    }

    public void createTTL(String path, byte[] data, long ttl, CreateMode mode) {
        try {
            curator.create().withTtl(ttl).creatingParentsIfNeeded().withMode(mode).forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode) + ttl, path, ZKCli.data(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode) + ttl, path, ZKCli.data(data)), e.getMessage());
        }
    }

    public void delete(String path) {
        try {
            curator.delete().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.delete, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.delete, path), e.getMessage());
        }
    }

    public void deletingChildrenIfNeeded(String path) {
        try {
            curator.delete().deletingChildrenIfNeeded().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.deleteall, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.deleteall, path), e.getMessage());
        }
    }

    public Stat checkExists(String path) {
        try {
            Stat stat = curator.checkExists().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.stat, path));
            return stat;
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.stat, path), e.getMessage());
        }
        return null;
    }

    public byte[] getData(String path) {
        try {
            byte[] data = curator.getData().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.get, path));
            return data;
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.get, path), e.getMessage());
        }
        return null;
    }

    public byte[] storingStatIn(String path, Stat stat) {
        try {
            return curator.getData().storingStatIn(stat).forPath(path);
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.get_s, path), e.getMessage());
        }
        return null;
    }

    public void setData(String path, byte[] data) {
        try {
            curator.setData().forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.set, path, ZKCli.data(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.set, path, ZKCli.data(data)), e.getMessage());
        }
    }

    public List<String> getChildren(String path) {
        try {
            return curator.getChildren().forPath(path);
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.ls, path), e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<ACL> getACL(String path) {
        try {
            return curator.getACL().forPath(path);
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.getAcl, path), e.getMessage());
        }
        return new ArrayList<>();
    }

    public void setACL(String path, List<ACL> aclList) {
        try {
            curator.setACL().withACL(aclList).forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(aclList, ",")));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(aclList, ",")), e.getMessage());
        }
    }
}
