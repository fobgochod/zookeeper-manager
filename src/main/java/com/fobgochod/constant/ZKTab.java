package com.fobgochod.constant;

import com.fobgochod.util.ZKIcons;
import com.fobgochod.util.ZKBundle;
import com.intellij.icons.AllIcons;

import javax.swing.*;

public enum ZKTab {

    Data(ZKBundle.message("zookeeper.tab.name.data"), ZKBundle.message("zookeeper.tab.tooltip.data"), AllIcons.Actions.SplitVertically),
    Stat(ZKBundle.message("zookeeper.tab.name.stat"), ZKBundle.message("zookeeper.tab.tooltip.stat"), AllIcons.Nodes.Editorconfig),
    ACL(ZKBundle.message("zookeeper.tab.name.acl"), ZKBundle.message("zookeeper.tab.tooltip.acl"), AllIcons.Diff.Lock),
    Log(ZKBundle.message("zookeeper.tab.name.log"), ZKBundle.message("zookeeper.tab.tooltip.log"), ZKIcons.NOTIFICATIONS);

    private final String key;
    private final String intro;
    private final Icon icon;

    ZKTab(String key, String intro, Icon icon) {
        this.key = key;
        this.intro = intro;
        this.icon = icon;
    }

    public String key() {
        return key;
    }

    public String intro() {
        return intro;
    }

    public Icon icon() {
        return icon;
    }
}
