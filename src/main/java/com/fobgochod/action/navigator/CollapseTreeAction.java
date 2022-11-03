package com.fobgochod.action.navigator;

import com.fobgochod.action.AbstractNodeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * collapse all tree node
 *
 * @author Xiao
 * @date 2022/10/23 19:36
 */
public class CollapseTreeAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        zooToolWindow.collapseTree();
    }
}
