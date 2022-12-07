package com.fobgochod.domain;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.setting.ZKConfigState;
import com.fobgochod.util.StringUtil;
import com.intellij.util.ui.tree.AbstractTreeModel;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ZooKeeper tree model
 *
 * @author fobgochod
 * @date 2022/10/16 0:54
 */
public class ZKTreeModel extends AbstractTreeModel {

    private static final ZKConfigState config = ZKConfigState.getInstance();
    private static final ZKClient zkClient = ZKClient.getInstance();
    private final ZKNode ROOT = new ZKNode(ZKConstant.SLASH, config.getTitle(), true);
    private List<String> paths = Arrays.asList();

    public ZKTreeModel() {
        if (StringUtil.isNotEmpty(config.getPaths())) {
            this.paths = Arrays.asList(config.getPaths().trim().split("[\\s;]+"));
        }
    }

    public static void fillNode(ZKNode zkNode) {
        fillData(zkNode);
        fillStat(zkNode);
        fillAcl(zkNode);
    }

    public static void fillData(ZKNode zkNode) {
        byte[] contents = zkClient.getData(zkNode.getFullPath());
        zkNode.setData(contents);
    }

    public static void fillStat(ZKNode zkNode) {
        Stat stat = zkClient.checkExists(zkNode.getFullPath());
        zkNode.setStat(stat);
    }

    public static void fillAcl(ZKNode zkNode) {
        List<ACL> perms = zkClient.getACL(zkNode.getFullPath());
        zkNode.setPerms(perms.stream().map(ZKAcl::new).collect(Collectors.toList()));
    }


    @Override
    public Object getRoot() {
        if (ROOT.getStat() != null) {
            fillNode(ROOT);
        }
        return ROOT;
    }

    @Override
    public Object getChild(Object parent, int index) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return children.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        ZKNode zkNode = (ZKNode) parent;
        if (zkNode.getStat() == null) {
            fillNode(zkNode);
        }
        if (!zkNode.isLeaf() && !paths.isEmpty()) {
            return getChildren(zkNode).size();
        }
        return zkNode.getChildrenCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        ZKNode zkNode = (ZKNode) node;
        if (zkNode.getStat() == null) {
            fillNode(zkNode);
        }
        return zkNode.isLeaf();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return IntStream.range(0, children.size()).filter(i -> ((ZKNode) child).getFullPath().equals(children.get(i).getFullPath())).findFirst().orElse(-1);
    }

    public List<ZKNode> getChildren(ZKNode zkNode) {
        List<ZKNode> children = new ArrayList<>();
        if (zkNode.getStat() == null) {
            fillNode(zkNode);
        }

        if (zkNode.isLeaf()) {
            return children;
        }

        List<String> childPaths = zkClient.getChildren(zkNode.getFullPath());
        childPaths.sort(String::compareTo);
        for (String childPath : childPaths) {
            ZKNode childNode = new ZKNode(zkNode.getFullPath(), childPath);
            if (isWhiteListPath(childNode.getFullPath())) {
                children.add(childNode);
            }
        }
        return children;
    }

    private boolean isWhiteListPath(String filePath) {
        if (this.paths.isEmpty()) {
            return true;
        }
        boolean legal = false;
        for (String whitePath : paths) {
            if (filePath.startsWith(whitePath)) {
                legal = true;
                break;
            } else if (whitePath.lastIndexOf("/") > 1 && whitePath.startsWith(filePath)) {
                legal = true;
                break;
            }
        }
        return legal;
    }
}
