package com.fobgochod.action.navigator;

import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import org.jetbrains.annotations.NotNull;

/**
 * edit ZooKeeper connection settings
 *
 * @author linux_china
 */
public class EditSettingsAction extends AnAction {

    public EditSettingsAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.edit.settings.text"));
        getTemplatePresentation().setIcon(AllIcons.General.Settings);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        ShowSettingsUtil.getInstance().showSettingsDialog(event.getProject(), ZKBundle.message("configurable.display.name"));
    }
}
