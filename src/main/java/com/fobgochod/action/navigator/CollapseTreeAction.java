package com.fobgochod.action.navigator;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * collapse all tree node
 *
 * @author fobgochod
 * @date 2022/10/23 19:36
 */
public class CollapseTreeAction extends AbstractNodeAction {

    public CollapseTreeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.collapse.all.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Collapseall);
    }


    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        toolWindow.collapseTree();
    }
}
