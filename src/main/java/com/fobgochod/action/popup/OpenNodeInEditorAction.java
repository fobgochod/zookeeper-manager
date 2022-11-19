package com.fobgochod.action.popup;

import com.fobgochod.domain.ZKNode;
import com.fobgochod.view.vfs.ZKNodeFile;
import com.fobgochod.view.vfs.ZKNodeFileSystem;
import com.fobgochod.view.window.ZooToolWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.jetbrains.annotations.NotNull;

/**
 * open node in editor
 *
 * @author fobgochod
 * @date 2022/10/15 22:26
 */
public class OpenNodeInEditorAction extends AnAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKNode selectionNode = ZooToolWindow.getInstance().getSelectionNode();
        if (selectionNode != null) {
            ZKNodeFile virtualFile = (ZKNodeFile) ZKNodeFileSystem.getInstance().findFileByPath(selectionNode.getFullPath());
            if (virtualFile != null && event.getProject() != null) {
                virtualFile.setLeaf();
                FileEditorManager.getInstance(event.getProject()).openFile(virtualFile, true);
            }
        }
    }
}
