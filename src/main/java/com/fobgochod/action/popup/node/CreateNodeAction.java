package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.StringUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.action.popup.CreateNodeUI;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * create node action
 * <p>
 * ${@link org.apache.zookeeper.CreateMode}
 *
 * @author fobgochod
 * @since 1.0
 */
public class CreateNodeAction extends NodeSelectedAction {

    public CreateNodeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.create.node.text"));
        getTemplatePresentation().setIcon(AllIcons.General.Add);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return;
        }
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode == null) {
            return;
        }

        final DialogBuilder builder = new DialogBuilder();
        builder.setTitle(ZKBundle.message("action.popup.create.node.text") + String.format("(%s)", selectionNode.getFullPath()));

        CreateNodeUI ui = new CreateNodeUI(event.getProject());
        builder.setPreferredFocusComponent(ui.getNodePath());
        builder.setCenterPanel(ui.getRoot());
        builder.setOkOperation(() -> {
            if (ui.getError() != null) {
                builder.setErrorText(ui.getError());
                return;
            }
            String nodePath = ui.getNodePath().getText();
            if (StringUtil.isNotEmpty(nodePath)) {

                String fullPath = StringUtil.rebuild(selectionNode.getFullPath(), nodePath);
                if (ui.getNodeMode().isTTL()) {
                    zkClient.create(fullPath, ui.getNodeData(), ui.getNodeMode(), ui.getTTL());
                } else {
                    zkClient.create(fullPath, ui.getNodeData(), ui.getNodeMode());
                }
                toolWindow.updateTree();
            }
            builder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
        });
        builder.showModal(true);
    }
}
