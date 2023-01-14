package com.fobgochod.constant;

public enum AclPermission {

    READ(Perms.READ, "READ", "you can get data from a node and list its children."),
    WRITE(Perms.WRITE, "WRITE", "you can set data for a node."),
    CREATE(Perms.CREATE, "CREATE", "you can create a child node."),
    DELETE(Perms.DELETE, "DELETE", "you can delete a child node."),
    ADMIN(Perms.ADMIN, "ADMIN", "you can set permissions."),
    ALL(Perms.ALL, "ALL", "you can do anything.");

    private final int id;
    private final String key;
    private final String intro;

    AclPermission(int id, String key, String intro) {
        this.id = id;
        this.key = key;
        this.intro = intro;
    }

    public static AclPermission of(int perm) {
        for (AclPermission permission : values()) {
            if (permission.id == perm) {
                return permission;
            }
        }
        return null;
    }

    public String key() {
        return key;
    }

    public String intro() {
        return intro;
    }

    public interface Perms {

        int READ = 1;

        int WRITE = 1 << 1;

        int CREATE = 1 << 2;

        int DELETE = 1 << 3;

        int ADMIN = 1 << 4;

        int ALL = READ | WRITE | CREATE | DELETE | ADMIN;

    }
}
