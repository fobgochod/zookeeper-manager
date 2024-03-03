package com.fobgochod;

import com.fobgochod.constant.ZKCli;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.settings.ZKSettings;
import com.fobgochod.util.NoticeUtil;
import com.intellij.notification.NotificationType;
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

public class CuratorClient implements ZKClient {

    private CuratorFramework curator;

    public void close() {
        if (curator != null) {
            // 关闭旧连接
            curator.close();
        }
    }

    public boolean isConnected() {
        return curator != null && curator.getZookeeperClient().isConnected();
    }

    public boolean init() {
        ZKSettings state = ZKSettings.getInstance();
        return init(state.connectString(), state.getBlockUntilConnected(), state.getSaslClientEnabled());
    }

    public boolean init(String connectString, int maxWaitTime, boolean saslClientEnabled) {
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
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_data, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_data, path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public void create(String path, byte[] data, CreateMode mode) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public void create(String path, byte[] data, CreateMode mode, long ttl) {
        try {
            curator.create().withTtl(ttl).creatingParentsIfNeeded().withMode(mode).forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode) + ttl, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode) + ttl, path, ZKCli.getString(data)), e.getMessage());
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

    public void deleteChildren(String path) {
        try {
            curator.delete().deletingChildrenIfNeeded().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.deleteall, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.deleteall, path), e.getMessage());
        }
    }

    public void setData(String path, byte[] data) {
        try {
            curator.setData().forPath(path, data);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.set, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.set, path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public byte[] getData(String path) {
        try {
            byte[] data = curator.getData().forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.get, path));
            if (data != null) {
                // 通过cli执行create /hello  返回数据未null
                return data;
            }
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.get, path), e.getMessage());
        }
        return ZKConstant.EMPTY_BYTE;
    }

    public byte[] getData(String path, Stat stat) {
        try {
            byte[] data = curator.getData().storingStatIn(stat).forPath(path);
            if (data != null) {
                // 通过cli执行create /hello  返回数据未null
                return data;
            }
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.get_s, path), e.getMessage());
        }
        return ZKConstant.EMPTY_BYTE;
    }

    public Stat exists(String path) {
        try {
            return curator.checkExists().forPath(path);
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.stat, path), e.getMessage());
        }
        return null;
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

    public void setACL(String path, List<ACL> acl) {
        try {
            curator.setACL().withACL(acl).forPath(path);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(acl, ",")));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(acl, ",")), e.getMessage());
        }
    }
}
