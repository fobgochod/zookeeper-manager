package com.fobgochod.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ClientConnectedAction extends AbstractNodeAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        event.getPresentation().setEnabled(zkClient.isConnected());
    }
}
