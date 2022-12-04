package com.fobgochod.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public abstract class NodeSelectedAction extends AbstractNodeAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        event.getPresentation().setEnabledAndVisible(toolWindow.getSelectionNode() != null);
    }
}
