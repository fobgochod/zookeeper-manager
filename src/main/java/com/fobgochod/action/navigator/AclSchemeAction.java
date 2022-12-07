package com.fobgochod.action.navigator;

import com.fobgochod.action.ClientConnectedAction;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.action.navigator.AclSchemeUI;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.zookeeper.data.ClientInfo;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class AclSchemeAction extends ClientConnectedAction {

    public AclSchemeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.acl.schemes.text"));
        getTemplatePresentation().setIcon(AllIcons.CodeWithMe.CwmPermissions);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final DialogBuilder builder = new DialogBuilder();
        builder.setTitle(ZKBundle.message("action.toolbar.acl.schemes.text"));

        AclSchemeUI aclScheme = new AclSchemeUI();

        Vector<String> tableHeader2 = new Vector<>();
        tableHeader2.add("authScheme");
        tableHeader2.add("user");

        Vector<Vector<Object>> tableData2 = new Vector<>();
        for (ClientInfo clientInfo : zkClient.getClientInfo()) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(clientInfo.getAuthScheme());
            rowData.add(clientInfo.getUser());
            tableData2.add(rowData);
        }
        aclScheme.getAuthInfo().setModel(new DefaultTableModel(tableData2, tableHeader2));
        aclScheme.getAuthInfo().updateUI();

        builder.setCenterPanel(aclScheme.getRoot());
        builder.setOkOperation(() -> {
            zkClient.addAuthInfo(aclScheme.getScheme(), aclScheme.getAuth());
            NoticeUtil.info("add a authorized user [" + aclScheme.getUsername() + "]  success!");

            toolWindow.selectionNodeChanged();

            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
        });
        builder.showModal(true);
    }
}
