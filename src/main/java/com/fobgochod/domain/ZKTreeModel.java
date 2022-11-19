package com.fobgochod.domain;

import com.fobgochod.ZKClient;
import com.intellij.openapi.util.text.StringUtil;
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

    private static final ZKClient ZK_CLIENT = ZKClient.getInstance();
    private List<String> paths = Arrays.asList();

    public ZKTreeModel() {
    }

    public ZKTreeModel(String paths) {
        if (StringUtil.isNotEmpty(paths)) {
            this.paths = Arrays.asList(paths.trim().split("[\\s;]+"));
        }
    }

    public static void fillNode(ZKNode node) {
        Stat stat = new Stat();
        byte[] contents = ZK_CLIENT.storingStatIn(node.getFullPath(), stat);
        if (contents != null) {
            node.setData(contents);
            node.setStat(stat);
            List<ACL> perms = ZK_CLIENT.getACL(node.getFullPath());
            node.setPerms(perms.stream().map(ZKAcl::new).collect(Collectors.toList()));
        }
    }

    @Override
    public Object getRoot() {
        if (ZKNode.ROOT.getStat() != null) {
            fillNode(ZKNode.ROOT);
        }
        return ZKNode.ROOT;
    }

    @Override
    public Object getChild(Object parent, int index) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return children.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        ZKNode zooNode = (ZKNode) parent;
        if (zooNode.getStat() == null) {
            fillNode(zooNode);
        }
        if (!zooNode.isLeaf() && !paths.isEmpty()) {
            return getChildren(zooNode).size();
        }
        return zooNode.getChildrenCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        ZKNode zooNode = (ZKNode) node;
        if (zooNode.getStat() == null) {
            fillNode(zooNode);
        }
        return zooNode.isLeaf();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return IntStream.range(0, children.size()).filter(i -> ((ZKNode) child).getFullPath().equals(children.get(i).getFullPath())).findFirst().orElse(-1);
    }

    public List<ZKNode> getChildren(ZKNode node) {
        List<ZKNode> children = new ArrayList<>();
        if (node.getStat() == null) {
            fillNode(node);
        }

        if (node.isLeaf()) {
            return children;
        }

        List<String> childPaths = ZK_CLIENT.getChildren(node.getFullPath());
        childPaths.sort(String::compareTo);
        for (String childPath : childPaths) {
            ZKNode childNode = new ZKNode(node.getFullPath(), childPath);
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
