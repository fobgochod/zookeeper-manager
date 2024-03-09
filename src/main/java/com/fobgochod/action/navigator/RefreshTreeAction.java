package com.fobgochod.action.navigator;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.AclScheme;
import com.fobgochod.settings.ZKSettings;
import com.fobgochod.util.StringUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Refresh ZooKeeper Tree
 *
 * @author fobgochod
 * @since 1.0
 */
public class RefreshTreeAction extends AnAction {

    public RefreshTreeAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.refresh.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.Refresh);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        if (event.getProject() == null) {
            return;
        }

        ZKClient zkClient = ZKClient.getInstance();
        ZKSettings state = ZKSettings.getInstance();
        boolean success = zkClient.init(state.connectString(), state.getSessionTimeout(), state.getEnableSasl());
        if (success) {
            if (state.getEnableSasl() && StringUtil.isNotEmpty(state.getUsername())) {
                zkClient.addAuthInfo(AclScheme.digest.name(), state.getUsername() + ":" + state.getPassword());
            }
            ZKToolWindow.getInstance(event.getProject()).initTree();
        }
    }
}
