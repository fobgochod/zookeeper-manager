package com.fobgochod.action;

import com.fobgochod.ZKClient;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNodeAction extends AnAction {

    protected final ZKClient zkClient = ZKClient.getInstance();
    protected ZKToolWindow toolWindow;

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project != null && this.toolWindow == null) {
            this.toolWindow = ZKToolWindow.getInstance(project);
        }
    }
}
