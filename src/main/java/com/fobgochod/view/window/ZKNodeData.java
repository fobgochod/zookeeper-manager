package com.fobgochod.view.window;

import com.fobgochod.constant.AclPermission;
import com.fobgochod.constant.StatStructure;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.domain.ZKTreeModel;
import com.intellij.openapi.project.Project;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.DefaultTableModel;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class ZKNodeData {

    private static final Object lock = new Object();
    private static volatile ZKNodeData instance;
    private final ZKToolWindow toolWindow;

    private ZKNodeData(@NotNull Project project) {
        this.toolWindow = ZKToolWindow.getInstance(project);
    }

    public static ZKNodeData getInstance(@NotNull Project project) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ZKNodeData(project);
                }
            }
        }
        return instance;
    }

    private static void setAcl(ACL acl, int perm, Vector<Vector<Object>> row) {
        if ((acl.getPerms() & perm) != 0) {
            Vector<Object> item = new Vector<>();
            item.add(acl.getId().getScheme());
            item.add(acl.getId().getId());
            item.add(AclPermission.of(perm));
            row.add(item);
        }
    }

    public void showTabData(ZKNode node) {
        ZKTreeModel.fillData(node);
        toolWindow.setData(new String(node.getData()));
    }

    public void showTabStat(ZKNode node) {
        ZKTreeModel.fillStat(node);

        Stat stat = node.getStat();
        if (stat == null) {
            toolWindow.setStat(new DefaultTableModel());
            return;
        }

        long ctime = stat.getCtime();
        long mtime = stat.getMtime();
        String ctimeFormat = Instant.ofEpochMilli(ctime).atZone(ZoneOffset.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String mtimeFormat = Instant.ofEpochMilli(mtime).atZone(ZoneOffset.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        DefaultTableModel statModel = new DefaultTableModel(
                new Object[][]{
                        {StatStructure.czxid.key(), stat.getCzxid()},
                        {StatStructure.mzxid.key(), stat.getMzxid()},
                        {StatStructure.pzxid.key(), stat.getPzxid()},
                        {StatStructure.ctime.key(), ctimeFormat},
                        {StatStructure.mtime.key(), mtimeFormat},
                        {StatStructure.version.key(), stat.getVersion()},
                        {StatStructure.cversion.key(), stat.getCversion()},
                        {StatStructure.aversion.key(), stat.getAversion()},
                        {StatStructure.ephemeralOwner.key(), stat.getEphemeralOwner()},
                        {StatStructure.dataLength.key(), stat.getDataLength()},
                        {StatStructure.numChildren.key(), stat.getNumChildren()},
                },
                new String[]{
                        "key", "value"
                }
        );
        toolWindow.setStat(statModel);
    }

    public void showTabAcl(ZKNode node) {
        ZKTreeModel.fillAcl(node);

        Vector<String> tableHeader = new Vector<>();
        tableHeader.add("scheme");
        tableHeader.add("id");
        tableHeader.add("privilege");

        Vector<Vector<Object>> tableData = new Vector<>();
        for (ACL acl : node.getPerms()) {
            setAcl(acl, ZooDefs.Perms.CREATE, tableData);
            setAcl(acl, ZooDefs.Perms.DELETE, tableData);
            setAcl(acl, ZooDefs.Perms.WRITE, tableData);
            setAcl(acl, ZooDefs.Perms.READ, tableData);
            setAcl(acl, ZooDefs.Perms.ADMIN, tableData);
        }

        toolWindow.setAcl(new DefaultTableModel(tableData, tableHeader));
    }
}
