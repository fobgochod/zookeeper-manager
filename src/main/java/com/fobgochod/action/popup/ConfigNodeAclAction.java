package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.view.action.popup.ConfigNodeAclUI;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * config node ACLs
 *
 * @author fobgochod
 * @date 2022/10/23 19:04
 */
public class ConfigNodeAclAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        final DialogBuilder builder = new DialogBuilder();
        builder.setTitle("ACLs");

        ZKNode selectionNode = zooToolWindow.getSelectionNode();

        ConfigNodeAclUI ui = new ConfigNodeAclUI(selectionNode.getPerms());
        builder.setPreferredFocusComponent(ui.getScheme());
        builder.setCenterPanel(ui.getRoot());
        builder.setOkOperation(() -> {
            zkClient.setACL(selectionNode.getFullPath(), ui.getData());
            selectionNode.setPerms(ui.getData());
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
        });
        builder.showModal(true);
    }
}
