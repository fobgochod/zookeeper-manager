package com.fobgochod.domain;

import com.fobgochod.constant.ZKConstant;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ZooKeeper Node
 *
 * <a href="https://zookeeper.apache.org/doc/r3.8.0/zookeeperProgrammers.html#sc_zkDataModel_znodes">ZNodes<a/>
 *
 * @author fobgochod
 * @date 2022/10/16 1:00
 */
public class ZKNode {

    private static final List<String> binaryExtNames = Arrays.asList("pb", "bin", "msgpack");
    private boolean root;
    private String path;
    private String fullPath;
    private String name;
    private byte[] data;
    private Stat stat;
    private List<? extends ACL> perms = new ArrayList<>();

    private ZKNode() {
    }

    public ZKNode(String path, String name) {
        this(path, name, false);
    }

    public ZKNode(String path, String name, boolean root) {
        this.path = path;
        this.name = name;
        this.root = root;
        if (root) {
            this.fullPath = path;
        } else {
            if (ZKConstant.SLASH.equals(path)) {
                this.fullPath = path + name;
            } else {
                this.fullPath = path + ZKConstant.SLASH + name;
            }
        }
    }

    public String getPath() {
        return path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public List<? extends ACL> getPerms() {
        return perms;
    }

    public void setPerms(List<? extends ACL> perms) {
        this.perms = perms;
    }

    public boolean isRoot() {
        return root;
    }

    public boolean isLeaf() {
        return stat != null && stat.getNumChildren() == 0;
    }

    public boolean isEphemeral() {
        return stat != null && stat.getEphemeralOwner() > 0;
    }

    public int getChildrenCount() {
        return stat != null ? stat.getNumChildren() : 0;
    }

    public boolean isBinary() {
        String extName = null;
        if (name.contains(".")) {
            extName = name.substring(name.lastIndexOf(".") + 1);
        }
        return extName != null && binaryExtNames.contains(extName.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
