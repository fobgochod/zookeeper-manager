package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * open node in editor
 *
 * @author fobgochod
 * @since 1.0
 */
public class OpenNodeInEditorAction extends NodeSelectedAction {

    public OpenNodeInEditorAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.open.editor.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.SplitVertically);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return;
        }
        ZKToolWindow.getInstance(event.getProject()).openFile(true);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        ZKNode selectionNode = toolWindow.getSelectionNode();
        event.getPresentation().setEnabledAndVisible(selectionNode != null && !selectionNode.isRoot());
    }
}
