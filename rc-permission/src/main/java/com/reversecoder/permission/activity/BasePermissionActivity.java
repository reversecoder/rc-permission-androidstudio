package com.reversecoder.permission.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.reversecoder.permission.R;
import com.reversecoder.permission.dialog.DialogText;
import com.reversecoder.permission.engine.PermissionConfig;
import com.reversecoder.permission.engine.PermissionManager;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.PermissionResultCallback;

import java.util.HashMap;

/**
 * @author Md. Rashsadul Alam
 */
public class BasePermissionActivity extends AppCompatActivity implements PermissionResultCallback {

    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionConfig permissionConfig = new PermissionConfig.Builder()
                .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                    put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
                    put(Manifest.permission_group.CONTACTS, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
                }})
                .build();

        PermissionConfig.initDefault(permissionConfig);

        permissionManager = new PermissionManager(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        permissionManager.getLifecycleHandler().onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        permissionManager.getLifecycleHandler().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionManager.getLifecycleHandler().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status) {
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }


    public void showRationaleSnackbar(final int callId, int rationaleTextResID) {
        Snackbar
                .make(findViewById(android.R.id.content), rationaleTextResID, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPermissionManager().onRationaleConfirmed(callId);
                    }
                })
                .show();
    }

}
