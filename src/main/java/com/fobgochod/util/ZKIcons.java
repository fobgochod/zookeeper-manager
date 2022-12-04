package com.fobgochod.util;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <a href="https://plugins.jetbrains.com/docs/intellij/work-with-icons-and-images.html#how-to-organize-and-how-to-use-icons">How to organize and how to use icons?<a/>
 * <br/>
 * <a href="https://plugins.jetbrains.com/docs/intellij/plugin-icon-file.html">Plugin Logo<a/>
 * <br/>
 * <a href="https://bjansen.github.io/intellij-icon-generator">IntelliJ Icon Generator</a>
 *
 * @author fobgochod
 * @date 2022/10/23 22:19
 */
public class ZKIcons {

    public static final Icon ZOOKEEPER = load("/icons/zookeeper.svg");
    public static final Icon NOTIFICATIONS = load("/icons/notifications.svg");

    public static final Icon PERSISTENT = load("/icons/node/persistent.svg");
    public static final Icon PERSISTENT_SEQUENTIAL = load("/icons/node/persistent_sequential.svg");
    public static final Icon EPHEMERAL = load("/icons/node/ephemeral.svg");
    public static final Icon EPHEMERAL_SEQUENTIAL = load("/icons/node/ephemeral_sequential.svg");
    public static final Icon CONTAINER = load("/icons/node/container.svg");
    public static final Icon PERSISTENT_WITH_TTL = load("/icons/node/persistent_with_ttl.svg");
    public static final Icon PERSISTENT_SEQUENTIAL_WITH_TTL = load("/icons/node/persistent_sequential_with_ttl.svg");


    private ZKIcons() {
    }

    @NotNull
    public static Icon load(@NotNull String path) {
        return IconLoader.getIcon(path, ZKIcons.class);
    }
}
