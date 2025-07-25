package com.fobgochod.action.navigator;

import com.fobgochod.action.ClientConnectedAction;
import com.fobgochod.settings.ZKSettings;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.zookeeper.server.admin.JettyAdminServer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;

/**
 * <a href="https://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_adminserver">The AdminServer</a>
 * is an embedded Jetty server that provides an HTTP interface to the four-letter word commands.
 * {@link JettyAdminServer}
 *
 * @author fobgochod
 * @since 1.0
 */
public class CommandsUrlAction extends ClientConnectedAction {

    public CommandsUrlAction() {
        getTemplatePresentation().setText(ZKBundle.message("action.toolbar.commands.url.text"));
        getTemplatePresentation().setIcon(AllIcons.Actions.InlayGlobe);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(ZKSettings.getInstance().adminServerUrl()));
            } else {
                NoticeUtil.message("Desktop browsing is not supported on this platform.");
            }
        } catch (Exception ex) {
            NoticeUtil.message("Error occurred while opening URL: " + ex.getMessage());
        }
    }
}
