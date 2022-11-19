package com.fobgochod.action.navigator;

import com.fobgochod.action.AbstractNodeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * expand all tree node
 *
 * @author fobgochod
 * @date 2022/10/23 19:36
 */
public class ExpandTreeAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        zooToolWindow.expandTree();
    }
}
