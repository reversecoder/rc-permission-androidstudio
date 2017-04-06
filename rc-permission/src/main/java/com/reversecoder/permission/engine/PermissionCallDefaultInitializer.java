package com.reversecoder.permission.engine;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.reversecoder.permission.dialog.DialogText;

/**
 * @author Md. Rashsadul Alam
 */
class PermissionCallDefaultInitializer {

    public static final String TAG = "rc-permission-handler";

    void initializeWithDefault(@NonNull Context context, @NonNull String permission, @NonNull PermissionCallOptions callOptions, PermissionConfig permissionConfig) {
        DialogText text = getPermissionDefaultText(context, permission, permissionConfig);
        setDefaultDenyMessageIfNeeded(callOptions, text);
        setDefaultRationalMessageIfNeeded(callOptions, text);
    }

    @NonNull
    private DialogText getPermissionDefaultText(Context context, String permission, PermissionConfig permissionConfig) {
        String permissionGroup = getPermissionGroup(context, permission);
        DialogText text = null;

        if (permissionGroup != null) {
            text = permissionConfig.getDefaultTextForPermissions().get(permissionGroup);
        }

        return text != null ? text : permissionConfig.getPermissionTextFallback();
    }

    private void setDefaultDenyMessageIfNeeded(PermissionCallOptions callOptions, DialogText dialogText) {
        if (callOptions.showDenyDialog() && callOptions.getDenyDialogMsg() == null && callOptions.getDenyDialogMsgRes() == 0) {
            callOptions.setDenyDialogMsgRes(dialogText.denyDialogMsgRes);
        }
    }

    private void setDefaultRationalMessageIfNeeded(PermissionCallOptions callOptions, DialogText dialogText) {
        if (callOptions.showRationaleDialog() && callOptions.getRationaleDialogMsg() == null && callOptions.getRationaleDialogMsgRes() == 0) {
            callOptions.setRationaleDialogMsgRes(dialogText.rationaleDialogMsgRes);
        }
    }

    private String getPermissionGroup(Context context, String permission) {
        try {
            return context.getPackageManager().getPermissionInfo(permission, 0).group;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to get permission group", e);
            return null;
        }
    }

}
