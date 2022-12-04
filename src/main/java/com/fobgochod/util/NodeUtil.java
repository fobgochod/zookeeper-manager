package com.fobgochod.util;

import com.fobgochod.domain.ZKNode;
import org.apache.zookeeper.CreateMode;

import javax.swing.*;

public class NodeUtil {

    /**
     * 是否是有序节点
     * The counter has a format of %010d -- that is 10 digits with 0 (zero) padding (the counter is formatted in this way to simplify sorting), i.e. "0000000001".
     *
     * @param name node name
     * @return the node is sequence node
     */
    public static boolean isSequence(String name) {
        try {
            if (name.length() > 10) {
                String seqStr = name.substring(name.length() - 10);
                Integer.parseInt(seqStr);
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    /**
     * 获取节点类型
     */
    public static CreateMode mode(ZKNode node) {
        boolean isSequence = NodeUtil.isSequence(node.getName());
        if (node.isEphemeral()) {
            if (isSequence) {
                return CreateMode.EPHEMERAL_SEQUENTIAL;
            }
            return CreateMode.EPHEMERAL;
        } else {
            if (isSequence) {
                return CreateMode.PERSISTENT_SEQUENTIAL;
            }
            return CreateMode.PERSISTENT;
        }
    }

    /**
     * 获取节点图标
     */
    public static Icon getIcon(CreateMode mode) {
        switch (mode) {
            case PERSISTENT:
                return ZKIcons.PERSISTENT;
            case PERSISTENT_SEQUENTIAL:
                return ZKIcons.PERSISTENT_SEQUENTIAL;
            case EPHEMERAL:
                return ZKIcons.EPHEMERAL;
            case EPHEMERAL_SEQUENTIAL:
                return ZKIcons.EPHEMERAL_SEQUENTIAL;
            case CONTAINER:
                return ZKIcons.CONTAINER;
            case PERSISTENT_WITH_TTL:
                return ZKIcons.PERSISTENT_WITH_TTL;
            case PERSISTENT_SEQUENTIAL_WITH_TTL:
                return ZKIcons.PERSISTENT_SEQUENTIAL_WITH_TTL;
        }
        return ZKIcons.PERSISTENT;
    }
}
