package com.fobgochod.constant;

import java.awt.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class ZKConstant {

    public static final String DOT = ".";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SLASH = "/";
    public static final String HYPHEN = "-";
    public static final byte[] EMPTY_BYTE = new byte[0];
    public static final String LOCALHOST = "localhost";
    public static final String ZIP = ".zip";

    public static final Integer DEFAULT_CLIENT_PORT = 2181;
    public static final Integer DEFAULT_SESSION_TIMEOUT = 6 * 1000;

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * <a href="https://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_adminserver">The AdminServer</a>
     */
    public static final String COMMAND_URL = "http://localhost:8080/commands";
    public static final String COMMAND_ENVIRONMENT = SLASH + "environment";
    public static final String ZOOKEEPER_VERSION_KEY = "zookeeper.version";
    public static final Duration ADMIN_SERVER_TIMEOUT = Duration.ofSeconds(10);

    /**
     * 弹窗内容默认大小
     */
    public static final Dimension DIALOG_SIZE = new Dimension(460, 200);

    /**
     * Zookeeper ID
     */
    public static final String ZOOKEEPER_NAVIGATOR_TOOLBAR = "ZooKeeperNavigatorToolbar";
    public static final String ZOOKEEPER_NODE_POPUP = "ZooKeeperNodePopup";
    public static final String ZOOKEEPER_NODE_DATA_POPUP = "ZooKeeperNodeDataPopup";
    public static final String ZOOKEEPER_LOG_POPUP = "ZooKeeperLogPopup";

}
