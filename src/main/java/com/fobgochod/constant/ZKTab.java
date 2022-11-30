package com.fobgochod.constant;

import com.fobgochod.util.ZKBundle;

public enum ZKTab {

    Data(ZKBundle.message("zookeeper.tab.name.data"), ZKBundle.message("zookeeper.tab.tooltip.data")),
    Stat(ZKBundle.message("zookeeper.tab.name.stat"), ZKBundle.message("zookeeper.tab.tooltip.stat")),
    ACL(ZKBundle.message("zookeeper.tab.name.acl"), ZKBundle.message("zookeeper.tab.tooltip.acl")),
    Log(ZKBundle.message("zookeeper.tab.name.log"), ZKBundle.message("zookeeper.tab.tooltip.log"));

    private final String key;
    private final String intro;

    ZKTab(String key, String intro) {
        this.key = key;
        this.intro = intro;
    }

    public String key() {
        return key;
    }

    public String intro() {
        return intro;
    }
}
