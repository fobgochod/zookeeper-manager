package com.fobgochod.action.popup;

import com.fobgochod.action.AbstractNodeAction;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.domain.ZKNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * copy node path
 *
 * @author Xiao
 * @date 2022/10/15 22:21
 */
public class CopyNodePathAction extends AbstractNodeAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = zooToolWindow.getSelectionNode();
        if (selectionNode != null) {
            NoticeUtil.clipboard(selectionNode.getFullPath());
        }
    }
}
