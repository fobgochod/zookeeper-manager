package com.fobgochod.action.popup;

import com.fobgochod.ZKClient;
import com.fobgochod.util.ZKIcons;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.vfs.ZKNodeFile;
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
 * @date 2022/10/15 23:22
 */
public class UpdateDataInEditorAction extends EditorAction {

    public UpdateDataInEditorAction() {
        super(new EditorActionHandler() {

            @Override
            protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
                VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
                if (virtualFile instanceof ZKNodeFile) {
                    ZKNodeFile nodeFile = (ZKNodeFile) virtualFile;
                    ZKClient.getInstance().update(nodeFile, editor.getDocument().getText());

                    NoticeUtil.status("'" + nodeFile.getMyPath() + "' has been updated!");
                }
            }

            @Override
            protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
                VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
                return virtualFile instanceof ZKNodeFile;
            }
        });

        getTemplatePresentation().setText(ZKBundle.message("action.popup.update.data.editor.text"));
        getTemplatePresentation().setIcon(ZKIcons.ZOOKEEPER);
    }


}