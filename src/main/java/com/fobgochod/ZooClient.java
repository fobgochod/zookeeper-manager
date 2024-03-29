package com.fobgochod;

import com.fobgochod.constant.ZKCli;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKPaths;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.text.Strings;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.ClientInfo;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service(Service.Level.APP)
public final class ZooClient implements ZKClient {

    private ZooKeeper zooKeeper;

    public void close() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public boolean isConnected() {
        return zooKeeper != null && zooKeeper.getState().isConnected();
    }

    public boolean init(String connectString, int sessionTimeout, boolean enableClientSasl) {
        try {
            close();
            System.setProperty(ZKClientConfig.ENABLE_CLIENT_SASL_KEY, String.valueOf(enableClientSasl));
            CountDownLatch latch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, event -> {
                if (zooKeeper.getState().isConnected()) {
                    latch.countDown();
                }
            });
            if (latch.await(sessionTimeout, TimeUnit.MILLISECONDS) && isConnected()) {
                NoticeUtil.warn(connectString + " successfully connected!");
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
            zooKeeper.addAuthInfo(scheme, auth.getBytes());
            NoticeUtil.debug(ZKCli.getLog(ZKCli.addauth, scheme, auth));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.addauth, scheme, auth), e.getMessage());
        }
    }

    public List<ClientInfo> getClientInfo() {
        try {
            List<ClientInfo> clientInfos = zooKeeper.whoAmI();
            return clientInfos == null ? new ArrayList<>() : clientInfos;
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.whoami), e.getMessage());
        }
        return new ArrayList<>();
    }

    public void create(String path) {
        try {
            zooKeeper.create(path, ZKConstant.EMPTY_BYTE, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create, path), e.getMessage());
        }
    }

    public void create(String path, byte[] data) {
        try {
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_data, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_data, path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public void create(String path, byte[] data, CreateMode mode) {
        try {
            ZKPaths.createNodes(zooKeeper, path, data, mode, null, -1);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_data, ZKCli.mode(mode), path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public void create(String path, byte[] data, CreateMode mode, long ttl) {
        try {
            ZKPaths.createNodes(zooKeeper, path, data, mode, null, ttl);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.create_e_s_t_data, ZKCli.mode(mode), ttl, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.create_e_s_t_data, ZKCli.mode(mode), ttl, path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public void delete(String path) {
        try {
            ZKPaths.deleteChildren(zooKeeper, path, true);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.delete, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.delete, path), e.getMessage());
        }
    }

    public void deleteChildren(String path) {
        try {
            ZKPaths.deleteChildren(zooKeeper, path, true);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.deleteall, path));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.deleteall, path), e.getMessage());
        }
    }

    public void setData(String path, byte[] data) {
        try {
            zooKeeper.setData(path, data, -1);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.set, path, ZKCli.getString(data)));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.set, path, ZKCli.getString(data)), e.getMessage());
        }
    }

    public byte[] getData(String path) {
        try {
            if (isConnected()) {
                byte[] data = zooKeeper.getData(path, false, null);
                NoticeUtil.debug(ZKCli.getLog(ZKCli.get, path));
                if (data != null) {
                    // 通过cli执行create /hello  返回数据未null
                    return data;
                }
            }
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.get, path), e.getMessage());
        }
        return ZKConstant.EMPTY_BYTE;
    }

    public byte[] getData(String path, Stat stat) {
        try {
            byte[] data = zooKeeper.getData(path, false, stat);
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
            if (isConnected()) {
                Stat stat = zooKeeper.exists(path, false);
                if (stat != null) {
                    return stat;
                }
            }
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.stat, path), e.getMessage());
        }
        return ZKNode.STAT;
    }

    public List<String> getChildren(String path) {
        try {
            if (isConnected()) {
                return zooKeeper.getChildren(path, false);
            }
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.ls, path), e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<ACL> getACL(String path) {
        try {
            List<ACL> acl = zooKeeper.getACL(path, null);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.getAcl, path));
            return acl;
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.getAcl, path), e.getMessage());
        }
        return new ArrayList<>();
    }

    public void setACL(String path, List<ACL> acl) {
        try {
            zooKeeper.setACL(path, acl, -1);
            NoticeUtil.debug(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(acl, ",")));
        } catch (Exception e) {
            NoticeUtil.error(ZKCli.getLog(ZKCli.setAcl, path, Strings.join(acl, ",")), e.getMessage());
        }
    }
}
