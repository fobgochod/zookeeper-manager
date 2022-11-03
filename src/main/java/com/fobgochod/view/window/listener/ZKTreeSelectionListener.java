package com.fobgochod.view.window.listener;

import com.fobgochod.constant.AclPermission;
import com.fobgochod.constant.StatStructure;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.view.window.ZooToolWindow;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class ZKTreeSelectionListener implements TreeSelectionListener {

    private final ZooToolWindow toolWindow;

    public ZKTreeSelectionListener(ZooToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        Object currentNode = event.getPath().getLastPathComponent();
        if (currentNode != null) {
            showZNode((ZKNode) currentNode);
        }
    }


    public void showZNode(ZKNode node) {
        if (node.getData() != null) {
            toolWindow.getText().setText(new String(node.getData()));
        }

        Stat stat = node.getStat();
        if (stat != null) {
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

            toolWindow.getStat().setModel(statModel);
            toolWindow.getStat().updateUI();
        }

        Vector<String> tableHeader2 = new Vector<>();
        tableHeader2.add("scheme");
        tableHeader2.add("id");
        tableHeader2.add("privilege");

        Vector<Vector<Object>> tableData2 = new Vector<>();
        for (ACL acl : node.getPerms()) {
            setAcl(acl, ZooDefs.Perms.CREATE, tableData2);
            setAcl(acl, ZooDefs.Perms.DELETE, tableData2);
            setAcl(acl, ZooDefs.Perms.WRITE, tableData2);
            setAcl(acl, ZooDefs.Perms.READ, tableData2);
            setAcl(acl, ZooDefs.Perms.ADMIN, tableData2);
        }

        toolWindow.getAcl().setModel(new DefaultTableModel(tableData2, tableHeader2));
        toolWindow.getAcl().updateUI();
    }


    private void setAcl(ACL acl, int perm, Vector<Vector<Object>> row) {
        if ((acl.getPerms() & perm) != 0) {
            Vector<Object> item = new Vector<>();
            item.add(acl.getId().getScheme());
            item.add(acl.getId().getId());
            item.add(AclPermission.of(perm));
            row.add(item);
        }
    }
}
