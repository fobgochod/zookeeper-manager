package com.fobgochod.action.navigator;

import com.fobgochod.action.ClientConnectedAction;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * collapse all tree node
 *
 * @author fobgochod
 * @since  1.0
 */
public class CollapseTreeAction extends ClientConnectedAction {

    public CollapseTreeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.collapse.all.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Collapseall);
    }


    public void actionPerformed(@NotNull AnActionEvent event) {
        toolWindow.collapseTree();
    }
}
