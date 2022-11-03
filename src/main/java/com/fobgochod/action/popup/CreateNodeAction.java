package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.StringUtil;
import com.fobgochod.view.action.popup.CreateNodeUI;
import com.fobgochod.domain.ZKNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * create node action
 *
 * @author Xiao
 * @date 2022/10/15 20:39
 */
public class CreateNodeAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        final DialogBuilder builder = new DialogBuilder();
        builder.setTitle("Create Node");

        CreateNodeUI ui = new CreateNodeUI();
        builder.setPreferredFocusComponent(ui.getNodePath());
        builder.setCenterPanel(ui.getRoot());
        builder.setOkOperation(() -> {
            String nodePath = ui.getNodePath().getText();
            if (StringUtil.isNotEmpty(nodePath)) {

                ZKNode selectionNode = zooToolWindow.getSelectionNode();
                String fullPath = StringUtil.rebuild(selectionNode.getFullPath(), nodePath);

                zkClient.creatingParentsIfNeeded(fullPath, ui.getNodeData(), ui.getNodeMode());
                zooToolWindow.flushTree();
            }
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
        });
        builder.showModal(true);
    }
}
