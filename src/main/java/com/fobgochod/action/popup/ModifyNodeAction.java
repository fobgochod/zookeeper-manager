package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.domain.ZKNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * modify node data
 *
 * @author Xiao
 * @date 2022/10/15 20:39
 */
public class ModifyNodeAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = zooToolWindow.getSelectionNode();
        if (selectionNode != null) {
            byte[] updateData = zooToolWindow.getText().getText().getBytes();
            zkClient.setData(selectionNode.getFullPath(), updateData);
            selectionNode.setData(updateData);
        }
    }
}
