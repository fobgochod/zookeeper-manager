package com.fobgochod.util;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <a href="https://plugins.jetbrains.com/docs/intellij/work-with-icons-and-images.html#how-to-organize-and-how-to-use-icons">How to organize and how to use icons?<a/>
 * <br/>
 * <a href="https://plugins.jetbrains.com/docs/intellij/plugin-icon-file.html">Plugin Logo<a/>
 *
 * @author Xiao
 * @date 2022/10/23 22:19
 */
public class IconUtil {

    public static final Icon ZooKeeper = load("/icons/zookeeper_small.png");
    public static final Icon Logo = load("/icons/logo.svg");
    public static final Icon Root = IconLoader.getIcon("/icons/root.svg", IconUtil.class);
    public static final Icon Plugin = load("/META-INF/pluginIcon.svg");

    private IconUtil() {
    }

    @NotNull
    public static Icon load(@NotNull String path) {
        return IconManager.getInstance().getIcon(path, IconUtil.class);
    }
}
