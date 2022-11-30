package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.constant.ZKTab;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.domain.ZKTreeModel;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.action.popup.ConfigNodeAclUI;
import com.fobgochod.view.window.ZKNodeData;
import com.intellij.icons.AllIcons;
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

    public ConfigNodeAclAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.config.acl.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Show);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);

        final DialogBuilder builder = new DialogBuilder();
        builder.setTitle(ZKBundle.message("action.popup.config.acl.text"));

        ZKNode selectionNode = toolWindow.getSelectionNode();

        ConfigNodeAclUI ui = new ConfigNodeAclUI(selectionNode.getPerms());
        builder.setPreferredFocusComponent(ui.getScheme());
        builder.setCenterPanel(ui.getRoot());
        builder.setOkOperation(() -> {
            zkClient.setACL(selectionNode.getFullPath(), ui.getData());
            selectionNode.setPerms(ui.getData());
            if (toolWindow.getTab() == ZKTab.ACL) {
                ZKNodeData.getInstance(event.getProject()).showTabAcl(selectionNode);
            } else {
                toolWindow.showTab(ZKTab.ACL.ordinal());
            }
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
        });
        builder.showModal(true);
    }
}
