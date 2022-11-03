package com.fobgochod.action.navigator;

import com.fobgochod.ZKBundle;
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

    public void actionPerformed(@NotNull AnActionEvent event) {
        ShowSettingsUtil.getInstance().showSettingsDialog(event.getProject(), ZKBundle.message("setting.configurable.displayName"));
    }
}
