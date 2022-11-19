package com.fobgochod.action.navigator;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.AclScheme;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.domain.ZKTreeModel;
import com.fobgochod.setting.ZKConfigState;
import com.fobgochod.util.StringUtil;
import com.fobgochod.view.window.ZooToolWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Refresh ZooKeeper Tree
 *
 * @author fobgochod
 * @date 2022/10/15 22:37
 */
public class RefreshTreeAction extends AnAction {

    public void actionPerformed(@NotNull AnActionEvent event) {
        ZKConfigState config = ZKConfigState.getInstance();
        boolean reloadSuccess = ZKClient.getInstance().initZookeeper();
        if (reloadSuccess) {
            if (StringUtil.isNotEmpty(config.getUsername())) {
                ZKClient.getInstance().addAuthInfo(AclScheme.digest.name(), config.getUsername() + ":" + config.getPassword());
            }

            ZKNode.ROOT.setName(config.getTitle());
            ZooToolWindow.getInstance().initTree(new ZKTreeModel(config.getPaths()));
        }
    }
}
