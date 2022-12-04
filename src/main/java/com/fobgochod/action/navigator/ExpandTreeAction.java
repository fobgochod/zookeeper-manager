package com.fobgochod.action.navigator;

import com.fobgochod.action.ClientConnectedAction;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * expand all tree node
 *
 * @author fobgochod
 * @date 2022/10/23 19:36
 */
public class ExpandTreeAction extends ClientConnectedAction {

    public ExpandTreeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.expand.all.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Expandall);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        toolWindow.expandTree();
    }
}
