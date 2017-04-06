package com.reversecoder.permission.model;

/**
 * @author Md. Rashsadul Alam
 */
public interface PermissionResultCallback {
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status);
}