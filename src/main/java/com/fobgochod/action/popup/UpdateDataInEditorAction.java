package com.fobgochod.action.popup;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.ZKCli;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.util.ZKIcons;
import com.fobgochod.view.vfs.ZKNodeFile;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * modify node data
 *
 * @author fobgochod
 * @since 1.0
 */
public class UpdateDataInEditorAction extends EditorAction {

    public UpdateDataInEditorAction() {
        super(new Handler());

        getTemplatePresentation().setText(ZKBundle.message("action.popup.update.data.editor.text"));
        getTemplatePresentation().setIcon(ZKIcons.ZOOKEEPER);
    }

    private static boolean isZKNodeFile(DataContext dataContext) {
        VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        return virtualFile instanceof ZKNodeFile;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setVisible(isZKNodeFile(e.getDataContext()));
    }

    public static class Handler extends EditorActionHandler {
        @Override
        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
            VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
            if (virtualFile instanceof ZKNodeFile) {
                ZKNodeFile nodeFile = (ZKNodeFile) virtualFile;
                String data = editor.getDocument().getText();
                if (nodeFile.isSingleFileZip()) {
                    String replace = nodeFile.getName().replace(".zip", "");
                    ZKClient.getInstance().setData(nodeFile.getMyPath(), ZKNodeFile.zip(replace, ZKCli.getBytes(data)));
                } else {
                    ZKClient.getInstance().setData(nodeFile.getMyPath(), ZKCli.getBytes(data));
                }
                NoticeUtil.status("'" + nodeFile.getMyPath() + "' has been updated!");
            }
        }

        @Override
        protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
            return isZKNodeFile(dataContext);
        }
    }
}
