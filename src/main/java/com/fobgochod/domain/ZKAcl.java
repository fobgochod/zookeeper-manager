package com.fobgochod.domain;

import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

public class ZKAcl extends ACL {

    public ZKAcl(ACL acl) {
        super(acl.getPerms(), acl.getId());
    }

    public ZKAcl(int perms, Id id) {
        super(perms, id);
    }

    @Override
    public String toString() {
        return getId().getScheme() + ":" + getId().getId() + ":" + ZKUtil.getPermString(getPerms());
    }
}
