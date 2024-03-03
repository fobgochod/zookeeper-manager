package com.fobgochod.domain;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.settings.ZKSettings;
import com.intellij.util.ui.tree.AbstractTreeModel;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ZooKeeper tree model
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKTreeModel extends AbstractTreeModel {

    private static final ZKSettings state = ZKSettings.getInstance();
    private static final ZKClient zkClient = ZKClient.getInstance();
    private final ZKNode ROOT = new ZKNode(ZKConstant.SLASH, state.getTitle(), true);

    public ZKTreeModel() {
    }

    public static void fillData(ZKNode zkNode) {
        byte[] contents = zkClient.getData(zkNode.getFullPath());
        zkNode.setData(contents);
    }

    public static void fillStat(ZKNode zkNode) {
        if (!zkNode.isFill()) {
            Stat stat = zkClient.exists(zkNode.getFullPath());
            zkNode.setStat(stat);
        }
    }

    public static void fillAcl(ZKNode zkNode) {
        List<ACL> perms = zkClient.getACL(zkNode.getFullPath());
        zkNode.setPerms(perms.stream().map(ZKAcl::new).collect(Collectors.toList()));
    }

    @Override
    public Object getRoot() {
        return ROOT;
    }

    @Override
    public Object getChild(Object parent, int index) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return children.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return children.size();
    }

    @Override
    public boolean isLeaf(Object node) {
        ZKNode zkNode = (ZKNode) node;
        fillStat(zkNode);
        return zkNode.isLeaf();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        List<ZKNode> children = getChildren((ZKNode) parent);
        return IntStream.range(0, children.size()).filter(i -> ((ZKNode) child).getFullPath().equals(children.get(i).getFullPath())).findFirst().orElse(-1);
    }

    public List<ZKNode> getChildren(ZKNode parent) {
        List<String> childPaths = zkClient.getChildren(parent.getFullPath());
        childPaths.sort(String::compareTo);

        List<ZKNode> children = new ArrayList<>();
        for (String childPath : childPaths) {
            ZKNode childNode = new ZKNode(parent.getFullPath(), childPath);
            children.add(childNode);
        }
        return children;
    }
}
