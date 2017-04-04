package com.reversecoder.permission.model;

import java.util.UUID;

/**
 * Created by rashed on 4/4/17.
 */

public class ManifestPermission {

    private String name;
    private PermissionRequestStatus permissionRequestStatus;
    private String uuid;

    public ManifestPermission(String name, PermissionRequestStatus permissionRequestStatus) {
        this.name = name;
        this.permissionRequestStatus = permissionRequestStatus;
        uuid = UUID.randomUUID().toString();
    }

    public PermissionRequestStatus getPermissionRequestStatus() {
        return permissionRequestStatus;
    }

    public void setPermissionRequestStatus(PermissionRequestStatus permissionRequestStatus) {
        this.permissionRequestStatus = permissionRequestStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
