package com.reversecoder.permission.engine;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.reversecoder.permission.activity.BasePermissionActivity;
import com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment;
import com.reversecoder.permission.dialog.PermissionRationaleDialogFragment;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.PermissionResultCallback;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionManager {

    private static final String SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL = "pendingPermissionCalls";
    public static final String TAG = "rc-permission-handler";

    private BasePermissionActivity activity;
    private LifecycleHandler lifecycleHandler = new LifecycleHandler();
    private PermissionCallDefaultInitializer callOptionsInitializer = new PermissionCallDefaultInitializer();
    private HashMap<Integer, PendingPermissionCall> pendingPermissionCalls = new HashMap<>();
    private PermissionConfig permissionConfig;

    public PermissionManager(BasePermissionActivity activity) {
        this.activity = activity;
        this.permissionConfig = PermissionConfig.get();
    }

    public <T extends Fragment & PermissionResultCallback> void callWithPermission(T fragment, int callId, String permission) {
        callWithPermission(fragment, callId, permission, permissionConfig.getDefaultPermissionCallOptions());
    }

    public void callWithPermission(BasePermissionActivity activity, int callId, String permission) {
        callWithPermission(activity, callId, permission, permissionConfig.getDefaultPermissionCallOptions());
    }

    public <T extends Fragment & PermissionResultCallback> void callWithPermission(T fragment, int callId, String permission, PermissionCallOptions permissionCallOptions) {
        PermissionCallInternalData data = new PermissionCallInternalData();
        data.requestFromFragment = true;
        data.fragmentId = fragment.getId();
        data.callId = callId;
        data.permission = permission;

        PendingPermissionCall pendingPermissionCall = new PendingPermissionCall(permissionCallOptions, data);
        doCallWithPermission(fragment.getActivity(), fragment, pendingPermissionCall);
    }

    public void callWithPermission(BasePermissionActivity activity, int callId, String permission, PermissionCallOptions permissionCallOptions) {
        PermissionCallInternalData data = new PermissionCallInternalData();
        data.requestFromFragment = false;
        data.callId = callId;
        data.permission = permission;

        PendingPermissionCall pendingPermissionCall = new PendingPermissionCall(permissionCallOptions, data);
        doCallWithPermission(activity, activity, pendingPermissionCall);
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void onRationaleConfirmed(int callId) {
        PendingPermissionCall pendingPermissionCall = pendingPermissionCalls.get(callId);
        getLifecycleHandler().onRationaleDialogConfirm(pendingPermissionCall);
    }

    public LifecycleHandler getLifecycleHandler() {
        return lifecycleHandler;
    }

    private void doCallWithPermission(FragmentActivity activity, PermissionResultCallback callback, PendingPermissionCall pendingCall) {
        callOptionsInitializer.initializeWithDefault(activity, pendingCall.internalData.permission, pendingCall.options, permissionConfig);

        if (hasPermission(pendingCall.internalData.permission)) {
            callback.onCallWithPermissionResult(pendingCall.internalData.callId, PermissionRequestStatus.PERMISSION_GRANTED);
        } else {
            pendingPermissionCalls.put(pendingCall.internalData.callId, pendingCall);

            if (pendingCall.options.isRationaleEnabled() && ActivityCompat.shouldShowRequestPermissionRationale(activity, pendingCall.internalData.permission)) {
                if (pendingCall.options.showRationaleDialog()) {
                    PermissionRationaleDialogFragment.showDialog(activity.getSupportFragmentManager(), pendingCall);
                }
                callback.onCallWithPermissionResult(pendingCall.internalData.callId, PermissionRequestStatus.PERMISSION_RATIONALE);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{pendingCall.internalData.permission}, pendingCall.internalData.callId);
            }
        }
    }

    @Nullable
    private PermissionResultCallback getCallback(Activity activity) {
        if (activity instanceof PermissionResultCallback) {
            return (PermissionResultCallback) activity;
        }

        return null;
    }

    @Nullable
    private PermissionResultCallback getCallback(Fragment fragment) {
        if (fragment instanceof PermissionResultCallback) {
            return (PermissionResultCallback) fragment;
        }

        return null;
    }

    @Nullable
    private PermissionResultCallback getCallback(PendingPermissionCall pendingPermissionCall) {
        if (pendingPermissionCall.internalData.requestFromFragment) {
            Fragment requestingFragment = getRequestingFragment(pendingPermissionCall);
            if (requestingFragment != null) {
                return getCallback(requestingFragment);
            }
        } else {
            return getCallback(activity);
        }

        return null;
    }

    @Nullable
    private Fragment getRequestingFragment(PendingPermissionCall permissionCall) {
        return activity.getSupportFragmentManager().findFragmentById(permissionCall.internalData.fragmentId);
    }

    private static class PermissionCallInternalData implements Serializable {
        private int callId;
        private boolean requestFromFragment;
        private int fragmentId;
        private String permission;
    }

    public static class PendingPermissionCall implements Serializable {
        public final PermissionCallOptions options;
        public final PermissionCallInternalData internalData;

        public PendingPermissionCall(PermissionCallOptions options, PermissionCallInternalData internalData) {
            this.options = options;
            this.internalData = internalData;
        }
    }

    public class LifecycleHandler {

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            PendingPermissionCall pendingPermissionCall = pendingPermissionCalls.remove(requestCode);

            if (grantResults.length < 1) {
                Log.w(TAG, "Incorrect size of grant result array");
                return;
            }

            if (pendingPermissionCall == null) {
                Log.w(TAG, "Unable to find PendingPermissionCall");
                return;
            }

            boolean granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
            PermissionResultCallback callback = getCallback(pendingPermissionCall);

            if (callback == null) {
                Log.w(TAG, "Callback was null. Unable to dispatch permission result");
            } else {
                if (!granted && !showRationale) {
                    if (pendingPermissionCall.options.showDenyDialog()) {
                        PermissionDeniedInfoDialogFragment.showDialog(activity.getSupportFragmentManager(), pendingPermissionCall);
                    }
                }

                callback.onCallWithPermissionResult(requestCode,
                        granted ? PermissionRequestStatus.PERMISSION_GRANTED :
                                showRationale ? PermissionRequestStatus.PERMISSION_DENIED : PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
            }
        }

        @SuppressWarnings("unchecked")
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL)) {
                    pendingPermissionCalls = (HashMap<Integer, PendingPermissionCall>) savedInstanceState.getSerializable(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL);
                }
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            outState.putSerializable(SAVE_INSTANCE_KEY_PENDING_PERMISSION_CALL, pendingPermissionCalls);
        }

        public void onRationaleDialogConfirm(PendingPermissionCall permissionCall) {
            ActivityCompat.requestPermissions(activity, new String[]{permissionCall.internalData.permission}, permissionCall.internalData.callId);
        }

    }

}
