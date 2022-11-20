package com.fobgochod.constant;

import org.apache.zookeeper.CreateMode;

/**
 * ZooCli.java
 *
 * <a href="https://zookeeper.apache.org/doc/current/zookeeperCLI.html">ZooKeeper CLI</a>
 *
 * @author fobgochod
 * @date 2022/10/28 0:38
 */
public class ZKCli {

    public static final String addauth = "addauth %s %s";
    public static final String whoami = "whoami";
    public static final String create = "create %s";
    public static final String create_data = "create %s %s";
    public static final String create_e_s_data = "create %s %s %s";
    public static final String delete = "delete %s";
    public static final String deleteall = "deleteall %s";
    public static final String stat = "stat %s";
    public static final String get = "get %s";
    public static final String get_s = "get -s %s";
    public static final String set = "set %s %s";
    public static final String ls = "ls %s";
    public static final String getAcl = "getAcl %s";
    public static final String setAcl = "setAcl %s %s";

    public static String getLog(String cli, Object... args) {
        return String.format(cli, args);
    }

    public static String data(byte[] data) {
        return new String(data);
    }

    public static String mode(CreateMode mode) {
        switch (mode) {
            case PERSISTENT:
                break;
            case PERSISTENT_SEQUENTIAL:
                return "-s";
            case EPHEMERAL:
                return "-e";
            case EPHEMERAL_SEQUENTIAL:
                return "-e -s";
            case CONTAINER:
                return "-c";
            case PERSISTENT_WITH_TTL:
                return "-t ";
            case PERSISTENT_SEQUENTIAL_WITH_TTL:
                return "-e -t ";
        }
        return "";
    }
}
