package com.fobgochod.constant;

public enum AclScheme {

    world("world", "has a single id, anyone, that represents anyone."),
    auth("auth", "is a special scheme which ignores any provided expression and instead uses the current user, credentials, and scheme."),
    digest("digest", "uses a username:password string to generate MD5 hash which is then used as an ACL ID identity."),
    ip("ip", "uses the client host IP as an ACL ID identity."),
    x509("x509", "uses the client X500 Principal as an ACL ID identity.");

    private final String key;
    private final String intro;

    AclScheme(String key, String intro) {
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
