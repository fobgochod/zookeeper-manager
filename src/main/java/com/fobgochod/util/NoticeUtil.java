package com.fobgochod.util;

import com.fobgochod.constant.ZKConstant;
import com.fobgochod.constant.ZKStyle;
import com.fobgochod.view.window.ZKToolWindow;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.impl.status.StatusBarUtil;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class NoticeUtil {

    private static final ZKToolWindow toolWindow = ZKToolWindow.getInstance();
    private static final JTextPane console = toolWindow.getConsole();
    private static final Set<String> days = new HashSet<>();

    public static void status(String message) {
        StatusBarUtil.setStatusBarInfo(toolWindow.getProject(), message);
    }

    public static void message(String message) {
        Messages.showErrorDialog(toolWindow.getProject(), message, "Error");
    }

    public static void notify(String message, NotificationType type) {
        Notifications.Bus.notify(new Notification(ZKBundle.message("notification.group.zookeeper"), message, type));
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
        status(message);
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
    }

    public static void render(String content, ZKStyle style) {
        Document document = console.getDocument();
        try {
            String message = String.format("%s - %s" + System.lineSeparator(), LocalTime.now().format(ZKConstant.TIME), content);
            document.insertString(0, message, style.get());
            // 处理日期，每天插入一条
            String now = LocalDate.now().format(ZKConstant.DATE);
            if (days.isEmpty() || !days.contains(now)) {
                days.add(now);
                String newDay = StringUtils.center(now, 50, ZKConstant.HYPHEN);
                document.insertString(0, newDay + System.lineSeparator(), ZKStyle.ERROR.get());
            }
        } catch (BadLocationException ignored) {
        }
    }
}
