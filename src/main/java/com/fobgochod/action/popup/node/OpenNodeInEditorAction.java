package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * open node in editor
 *
 * @author fobgochod
 * @date 2022/10/15 22:26
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
}
