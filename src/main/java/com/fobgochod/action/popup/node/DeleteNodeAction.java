package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * delete node and children if they exist.
 *
 * @author fobgochod
 * @date 2022/10/15 22:21
 */
public class DeleteNodeAction extends NodeSelectedAction {

    public DeleteNodeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.delete.node.text"));
        getTemplatePresentation().setIcon(AllIcons.General.Remove);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode == null) {
            return;
        }

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle(ZKBundle.message("action.popup.delete.node.text"));
        JLabel jTextField = new JLabel("Path: " + selectionNode.getFullPath());
        builder.setCenterPanel(jTextField);
        builder.setOkOperation(() -> {
            zkClient.deletingChildrenIfNeeded(selectionNode.getFullPath());
            toolWindow.updateTree();
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);

            NoticeUtil.status("'" + selectionNode.getFullPath() + "' has been deleted!");
        });
        builder.showModal(true);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        ZKNode selectionNode = toolWindow.getSelectionNode();
        event.getPresentation().setEnabledAndVisible(selectionNode != null && !selectionNode.isRoot());
    }
}
