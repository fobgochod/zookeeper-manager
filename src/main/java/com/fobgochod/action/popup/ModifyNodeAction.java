package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * modify node data
 *
 * @author fobgochod
 * @date 2022/10/15 20:39
 */
public class ModifyNodeAction extends AbstractNodeAction {

    public ModifyNodeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.modify.node.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Refresh);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);

        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode != null) {
            byte[] updateData = toolWindow.getData().getBytes();
            zkClient.setData(selectionNode.getFullPath(), updateData);
            selectionNode.setData(updateData);
        }
    }
}
