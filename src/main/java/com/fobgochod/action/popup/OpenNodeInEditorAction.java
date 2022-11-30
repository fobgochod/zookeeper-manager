package com.fobgochod.action.popup;

import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * open node in editor
 *
 * @author fobgochod
 * @date 2022/10/15 22:26
 */
public class OpenNodeInEditorAction extends AnAction {

    public OpenNodeInEditorAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.open.editor.text"));
        getTemplatePresentation().setIcon(AllIcons.General.Inline_edit);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return;
        }
        ZKToolWindow.getInstance(event.getProject()).openFile(true);
    }
}
