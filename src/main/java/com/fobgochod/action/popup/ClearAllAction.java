package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ClearAllAction extends AbstractNodeAction {

    public ClearAllAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.clear.all.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.GC);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        toolWindow.getConsole().setText("");
    }
}
