package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.constant.ZKTab;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * modify node data
 *
 * @author fobgochod
 * @since 1.0
 */
public class ModifyNodeAction extends NodeSelectedAction {

    public ModifyNodeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.modify.node.text"));
        getTemplatePresentation().setIcon(AllIcons.General.Inline_edit_hovered);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode != null) {
            toolWindow.showTab(ZKTab.Data.ordinal());
        }
    }
}
