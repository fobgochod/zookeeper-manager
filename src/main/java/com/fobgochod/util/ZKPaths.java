package com.fobgochod.util;

import com.fobgochod.constant.ZKConstant;
import com.fobgochod.domain.ZKNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * ZKPaths {@link org.apache.curator.utils.ZKPaths}
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKPaths {

    /**
     * Zookeeper's path separator character.
     */
    private static final char PATH_SEPARATOR_CHAR = '/';

    // Hardcoded in {@link org.apache.zookeeper.server.PrepRequestProcessor}
    private static final int SEQUENTIAL_SUFFIX_DIGITS = 10;

    /**
     * 获取节点类型
     */
    public static CreateMode mode(ZKNode node) {
        boolean isSequence = isSequence(node.getName());
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
     * 是否是有序节点
     * The counter has a format of %010d
     * that is 10 digits with 0 (zero) padding (the counter is formatted in this way to simplify sorting).
     * i.e. "0000000001".
     *
     * @param name node name
     * @return the node is sequence node
     */
    private static boolean isSequence(String name) {
        try {
            if (name.length() > SEQUENTIAL_SUFFIX_DIGITS) {
                String suffix = name.substring(name.length() - SEQUENTIAL_SUFFIX_DIGITS);
                Integer.parseInt(suffix);
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    /**
     * 获取节点图标
     */
    public static Icon getIcon(CreateMode mode) {
        return switch (mode) {
            case PERSISTENT -> ZKIcons.PERSISTENT;
            case PERSISTENT_SEQUENTIAL -> ZKIcons.PERSISTENT_SEQUENTIAL;
            case EPHEMERAL -> ZKIcons.EPHEMERAL;
            case EPHEMERAL_SEQUENTIAL -> ZKIcons.EPHEMERAL_SEQUENTIAL;
            case CONTAINER -> ZKIcons.CONTAINER;
            case PERSISTENT_WITH_TTL -> ZKIcons.PERSISTENT_WITH_TTL;
            case PERSISTENT_SEQUENTIAL_WITH_TTL -> ZKIcons.PERSISTENT_SEQUENTIAL_WITH_TTL;
        };
    }


    /**
     * <h1>digest</h1>
     * uses a username:password string to generate MD5 hash which is then used as an ACL ID identity.
     * <p>
     * addauth digest username:password
     *
     * @param username authorized user username
     * @param password authorized user password
     */
    public static String getAclId(String username, String password) {
        try {
            String aclId = String.format("`%s:%s", username, password);
            byte[] digest = MessageDigest.getInstance("SHA1").digest(aclId.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recursively deletes children of a node.
     *
     * @param zookeeper  the client
     * @param path       path of the node to delete
     * @param deleteSelf flag that indicates that the node should also get deleted
     */
    public static void deleteChildren(ZooKeeper zookeeper, String path, boolean deleteSelf)
            throws InterruptedException, KeeperException {
        List<String> children;
        try {
            children = zookeeper.getChildren(path, null);
        } catch (KeeperException.NoNodeException e) {
            // someone else has deleted the node since we checked
            return;
        }
        for (String child : children) {
            String fullPath = makePath(path, child);
            deleteChildren(zookeeper, fullPath, true);
        }

        if (deleteSelf) {
            try {
                zookeeper.delete(path, -1);
            } catch (KeeperException.NotEmptyException e) {
                // someone has created a new child since we checked ... delete again.
                deleteChildren(zookeeper, path, true);
            } catch (KeeperException.NoNodeException e) {
                // ignore... someone else has deleted the node since we checked
            }
        }
    }

    public static void createNodes(ZooKeeper zooKeeper, String path, byte[] data,
                                   CreateMode mode, Stat stat, long ttl)
            throws KeeperException, InterruptedException {
        try {
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode, stat, ttl);
        } catch (KeeperException.NoNodeException e) {
            mkDirs(zooKeeper, path, mode, false);
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode, stat, ttl);
        }
    }

    public static void mkDirs(ZooKeeper zookeeper, String path, CreateMode mode, boolean makeLastNode)
            throws InterruptedException, KeeperException {
        // skip first slash, root is guaranteed to exist
        int pos = 1;
        do {
            pos = path.indexOf(PATH_SEPARATOR_CHAR, pos + 1);

            if (pos == -1) {
                if (makeLastNode) {
                    pos = path.length();
                } else {
                    break;
                }
            }

            String subPath = path.substring(0, pos);
            if (zookeeper.exists(subPath, false) == null) {
                try {
                    zookeeper.create(subPath, ZKConstant.EMPTY_BYTE, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
                } catch (KeeperException.NodeExistsException ignore) {
                    // ignore... someone else has created it since we checked
                }
            }
        } while (pos < path.length());
    }

    /**
     * Given a parent path and a child node, create a combined full path
     *
     * @param parent the parent
     * @param child  the child
     * @return full path
     */
    private static String makePath(String parent, String child) {
        // 2 is the maximum number of additional path separators inserted
        int maxPathLength = nullableStringLength(parent) + nullableStringLength(child) + 2;
        // Avoid internal StringBuilder's buffer reallocation by specifying the max path length
        StringBuilder path = new StringBuilder(maxPathLength);

        joinPath(path, parent, child);

        return path.toString();
    }

    private static int nullableStringLength(String s) {
        return s != null ? s.length() : 0;
    }

    /**
     * Given a parent and a child node, join them in the given {@link StringBuilder path}
     *
     * @param path   the {@link StringBuilder} used to make the path
     * @param parent the parent
     * @param child  the child
     */
    private static void joinPath(StringBuilder path, String parent, String child) {
        // Add parent piece, with no trailing slash.
        if ((parent != null) && (!parent.isEmpty())) {
            if (parent.charAt(0) != PATH_SEPARATOR_CHAR) {
                path.append(PATH_SEPARATOR_CHAR);
            }
            if (parent.charAt(parent.length() - 1) == PATH_SEPARATOR_CHAR) {
                path.append(parent, 0, parent.length() - 1);
            } else {
                path.append(parent);
            }
        }

        if ((child == null)
                || (child.isEmpty())
                || (child.length() == 1 && child.charAt(0) == PATH_SEPARATOR_CHAR)) {
            // Special case, empty parent and child
            if (path.isEmpty()) {
                path.append(PATH_SEPARATOR_CHAR);
            }
            return;
        }

        // Now add the separator between parent and child.
        path.append(PATH_SEPARATOR_CHAR);

        int childAppendBeginIndex;
        if (child.charAt(0) == PATH_SEPARATOR_CHAR) {
            childAppendBeginIndex = 1;
        } else {
            childAppendBeginIndex = 0;
        }

        int childAppendEndIndex;
        if (child.charAt(child.length() - 1) == PATH_SEPARATOR_CHAR) {
            childAppendEndIndex = child.length() - 1;
        } else {
            childAppendEndIndex = child.length();
        }

        // Finally, add the child.
        path.append(child, childAppendBeginIndex, childAppendEndIndex);
    }
}
