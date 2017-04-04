package com.reversecoder.permission.model;

import java.util.UUID;

/**
 * Created by rashed on 4/4/17.
 */

public class ManifestPermission {

    private String name;
    private boolean isGranted;
    private String uuid;

    public ManifestPermission(String name, boolean isGranted) {
        this.name = name;
        this.isGranted = isGranted;
        uuid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
