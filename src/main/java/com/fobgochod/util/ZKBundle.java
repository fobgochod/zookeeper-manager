package com.fobgochod.util;

import com.intellij.AbstractBundle;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Locale;
import java.util.ResourceBundle;

public class ZKBundle extends AbstractBundle {

    @NonNls
    public static final String RESOURCE = "i18n.messages";

    @NotNull
    private static final ZKBundle INSTANCE = new ZKBundle();

    private ZKBundle() {
        super(RESOURCE);
    }

    @Nls
    @NotNull
    public static String message(@PropertyKey(resourceBundle = RESOURCE) String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    @Override
    protected @NotNull ResourceBundle findBundle(@NotNull @NonNls String pathToBundle,
                                                 @NotNull ClassLoader loader,
                                                 @NotNull ResourceBundle.Control control) {
        final String chineseLanguagePlugin = "com.intellij.zh";
        if (!PluginManager.isPluginInstalled(PluginId.getId(chineseLanguagePlugin))) {
            // 未安装 IDE中文语言包 插件则使用默认
            return ResourceBundle.getBundle(pathToBundle, Locale.ROOT, loader, control);
        }
        return ResourceBundle.getBundle(pathToBundle, Locale.getDefault(), loader, control);
    }
}
