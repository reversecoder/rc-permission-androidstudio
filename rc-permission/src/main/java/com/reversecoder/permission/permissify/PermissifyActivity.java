package com.reversecoder.permission.permissify;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.model.PermissionRequestStatus;

import java.util.HashMap;

/**
 * Base activity for an application that uses Permissify library. It provides PermissifyManager that handles various permission request states.
 */
public class PermissifyActivity extends AppCompatActivity implements PermissifyManager.Callback {

    private PermissifyManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
                .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
//                    put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
//                    put(Manifest.permission_group.CONTACTS, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
                }})
                .build();

        PermissifyConfig.initDefault(permissifyConfig);

        permissionManager = new PermissifyManager(this);
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

    /**
     * Gets PermissifyManager that is associated with this activity
     *
     * @return PermissifyManager
     */
    public PermissifyManager getPermissifyManager() {
        return permissionManager;
    }


    private void showRationaleSnackbar(final int callId, int rationaleTextResID) {
        Snackbar
                .make(findViewById(android.R.id.content), rationaleTextResID, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPermissifyManager().onRationaleConfirmed(callId);
                    }
                })
                .show();
    }

}
