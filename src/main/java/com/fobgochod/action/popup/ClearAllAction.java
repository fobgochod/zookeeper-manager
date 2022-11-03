package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ClearAllAction extends AbstractNodeAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        zooToolWindow.getConsole().setText("");
    }
}
