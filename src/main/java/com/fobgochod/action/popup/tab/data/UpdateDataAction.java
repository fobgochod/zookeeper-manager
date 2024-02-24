package com.fobgochod.action.popup.tab.data;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * update node data
 *
 * @author fobgochod
 * @since 1.0
 */
public class UpdateDataAction extends AbstractNodeAction {

    public UpdateDataAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.update.data.text"));
        getTemplatePresentation().setIcon(AllIcons.General.InlineRefreshHover);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode != null) {
            byte[] updateData = toolWindow.getData().getBytes();
            zkClient.setData(selectionNode.getFullPath(), updateData);
            selectionNode.setData(updateData);
        }
    }
}
