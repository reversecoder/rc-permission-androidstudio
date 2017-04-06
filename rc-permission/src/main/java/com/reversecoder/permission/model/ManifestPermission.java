package com.reversecoder.permission.model;

import java.util.Random;

/**
 * @author Md. Rashsadul Alam
 */
public class ManifestPermission {

    private String name;
    private PermissionRequestStatus permissionRequestStatus = PermissionRequestStatus.UNKNOWN;
    private int uuid;

    public ManifestPermission(String name, PermissionRequestStatus permissionRequestStatus) {
        this.name = name;
        this.permissionRequestStatus = permissionRequestStatus;
        uuid = getRandomNumber();
    }

    public PermissionRequestStatus getPermissionRequestStatus() {
        return permissionRequestStatus;
    }

    public void setPermissionRequestStatus(PermissionRequestStatus permissionRequestStatus) {
        this.permissionRequestStatus = permissionRequestStatus;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return name.replace("android.permission.", "").replace("com.android.launcher.permission.", "");
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }


    private int getRandomNumber() {
        Random r = new Random();
        int a = r.nextInt((1000 - 100)) + 100;
        return a;
    }
}
