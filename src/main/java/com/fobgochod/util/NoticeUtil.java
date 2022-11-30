package com.fobgochod.util;

import com.fobgochod.constant.ZKConstant;
import com.fobgochod.constant.ZKStyle;
import com.fobgochod.constant.ZKTab;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.wm.impl.status.StatusBarUtil;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDateTime;

public class NoticeUtil {

    private static final ZKToolWindow toolWindow = ZKToolWindow.getInstance();
    private static final JTextPane console = toolWindow.getConsole();

    public static void status(String message) {
        StatusBarUtil.setStatusBarInfo(toolWindow.getProject(), message);
        info(message);
    }

    public static void notify(String message, NotificationType type) {
        Notifications.Bus.notify(new Notification(ZKBundle.message("notification.group.zookeeper"), null, message, type));
        switch (type) {
            case ERROR:
                error(message);
                break;
            case WARNING:
                warn(message);
                break;
            default:
                info(message);
        }
    }

    public static void clipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        String message = "'" + content + "' has been copied into clipboard!";
        info(message);
    }

    public static void debug(String message) {
        render(message, ZKStyle.DEBUG);
    }

    public static void info(String message) {
        render(message, ZKStyle.INFO);
    }

    public static void warn(String message) {
        render(message, ZKStyle.WARN);
    }

    public static void error(String message) {
        render(message, ZKStyle.ERROR);
    }

    public static void error(String command, String message) {
        render(command + " - " + message, ZKStyle.ERROR);
        toolWindow.showTab(ZKTab.Log.ordinal());
    }

    public static void render(String content, ZKStyle style) {
        Document document = console.getDocument();
        try {
            String message = String.format("%s - %s\n", LocalDateTime.now().format(ZKConstant.DATETIME), content);
            document.insertString(0, message, style.get());
        } catch (BadLocationException ignored) {
        }
    }
}
