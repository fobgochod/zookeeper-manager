package com.fobgochod.action.popup.node;

import com.fobgochod.action.NodeSelectedAction;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * copy node path
 *
 * @author fobgochod
 * @date 2022/10/15 22:21
 */
public class CopyNodePathAction extends NodeSelectedAction {

    public CopyNodePathAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.popup.copy.path.text"));
        getTemplatePresentation().setIcon(AllIcons.General.InlineCopy);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode != null) {
            NoticeUtil.clipboard(selectionNode.getFullPath());
        }
    }
}
