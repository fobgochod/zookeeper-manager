package com.fobgochod.constant;

public enum StatStructure {

    czxid("czxid", "The zxid of the change that caused this znode to be created."),
    mzxid("mzxid", "The zxid of the change that last modified this znode."),
    pzxid("pzxid", "The zxid of the change that last modified children of this znode."),
    ctime("ctime", "The time in milliseconds from epoch when this znode was created."),
    mtime("mtime", "The time in milliseconds from epoch when this znode was last modified."),
    version("version", "The number of changes to the data of this znode."),
    cversion("cversion", "The number of changes to the children of this znode."),
    aversion("aversion", "The number of changes to the ACL of this znode."),
    ephemeralOwner("ephemeralOwner", "The session id of the owner of this znode if the znode is an ephemeral node. If it is not an ephemeral node, it will be zero."),
    dataLength("dataLength", "The length of the data field of this znode."),
    numChildren("numChildren", "The number of children of this znode.");

    private final String key;
    private final String intro;

    StatStructure(String key, String intro) {
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
