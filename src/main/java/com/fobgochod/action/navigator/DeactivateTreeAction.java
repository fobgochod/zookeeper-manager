package com.fobgochod.action.navigator;

import com.fobgochod.ZKClient;
import com.fobgochod.action.ClientConnectedAction;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Deactivate ZooKeeper
 *
 * @author fobgochod
 * @since 1.0
 */
public class DeactivateTreeAction extends ClientConnectedAction {

    public DeactivateTreeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.deactivate.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Suspend);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return;
        }

        ZKClient.getInstance().close();
        ZKToolWindow.getInstance(event.getProject()).closeTree();
    }
}
