package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.domain.ZKNode;
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
public class DeleteNodeAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = zooToolWindow.getSelectionNode();
        if (selectionNode == null) {
            return;
        }

        DialogBuilder builder = new DialogBuilder();
        builder.setTitle("Delete Node");
        JLabel jTextField = new JLabel("Path: " + selectionNode.getFullPath());
        builder.setCenterPanel(jTextField);
        builder.setOkOperation(() -> {
            zkClient.deletingChildrenIfNeeded(selectionNode.getFullPath());
            zooToolWindow.flushTree();
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);

            NoticeUtil.status("'" + selectionNode.getFullPath() + "' has been deleted!");
        });
        builder.showModal(true);
    }
}
