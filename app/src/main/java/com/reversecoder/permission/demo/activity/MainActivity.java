package com.reversecoder.permission.demo.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.reversecoder.permission.demo.R;
import com.reversecoder.permission.permissify.PermissifyActivity;
import com.reversecoder.permission.permissify.PermissifyManager;
import com.reversecoder.permission.permissify.PermissionCallOptions;

public class MainActivity extends PermissifyActivity {

    private static final int LOCATION_PERMISSION_REQUEST_ID = 1;
    private static final int CAMERA_PERMISSION_REQUEST_ID = 2;
    private static final int CONTACTS_PERMISSION_REQUEST_ID = 3;

    Button contactsButton;
    Button locationButton;
    Button cameraButton;

    TextView locationStatus;

    TextView cameraStatus;

    TextView contactsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initAction();
    }

    private void initUI() {
        contactsButton = (Button) findViewById(R.id.contacts_button);
        locationButton = (Button) findViewById(R.id.location_button);
        cameraButton = (Button) findViewById(R.id.camera_button);

        locationStatus = (TextView) findViewById(R.id.location_status);
        cameraStatus = (TextView) findViewById(R.id.camera_status);
        contactsStatus = (TextView) findViewById(R.id.contacts_status);

    }

    private void initAction() {

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call to permissify using default dialog text & behaviour
                getPermissifyManager().callWithPermission(MainActivity.this, LOCATION_PERMISSION_REQUEST_ID, android.Manifest.permission.ACCESS_FINE_LOCATION);

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call to permissify using default deny dialog & custom behaviour for rationale dialog (handled in onCallWithPermissionResult)
                getPermissifyManager().callWithPermission(MainActivity.this, CAMERA_PERMISSION_REQUEST_ID, Manifest.permission.CAMERA,
                        new PermissionCallOptions.Builder()
                                .withDefaultDenyDialog(true)
                                .withDefaultRationaleDialog(false)
                                .build()
                );
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call to permissify using without dialogs, but with custom behavior (handled in onCallWithPermissionResult)
                getPermissifyManager().callWithPermission(MainActivity.this, CONTACTS_PERMISSION_REQUEST_ID, Manifest.permission.READ_CONTACTS,
                        new PermissionCallOptions.Builder()
                                .withDefaultDenyDialog(false)
                                .withRationaleEnabled(false)
                                .build());
            }
        });

    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissifyManager.CallRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        if (callId == LOCATION_PERMISSION_REQUEST_ID) {
            visualiseStatus(locationStatus, status);

            switch (status) {
                case PERMISSION_GRANTED:
                    //getUserLocation();
                    break;
                case PERMISSION_DENIED_ONCE:
                    //do some custom logic
                    break;
                case PERMISSION_DENIED_FOREVER:
                    //do some custom logic
                case SHOW_PERMISSION_RATIONALE:
                    //do some custom logic
            }
        } else if (callId == CAMERA_PERMISSION_REQUEST_ID) {
            visualiseStatus(cameraStatus, status);

            if (status == PermissifyManager.CallRequestStatus.SHOW_PERMISSION_RATIONALE) {
                showCameraRationaleSnackbar(callId);
            }

        } else if (callId == CONTACTS_PERMISSION_REQUEST_ID) {
            visualiseStatus(contactsStatus, status);

            contactsButton.setEnabled(status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_ONCE && status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_FOREVER);
        }
    }

    private void showCameraRationaleSnackbar(final int callId) {
        Snackbar
                .make(findViewById(android.R.id.content), R.string.camera_rationale, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPermissifyManager().onRationaleConfirmed(callId);
                    }
                })
                .show();
    }

    private String getStatusText(PermissifyManager.CallRequestStatus status) {
        switch (status) {
            case PERMISSION_GRANTED:
                return getString(R.string.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                return getString(R.string.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return getString(R.string.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                return getString(R.string.permission_status_rationale);
            default:
                return "";
        }
    }

    @ColorInt
    private int getStatusColor(PermissifyManager.CallRequestStatus status) {
        switch (status) {
            case PERMISSION_GRANTED:
                return ContextCompat.getColor(this, R.color.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                return ContextCompat.getColor(this, R.color.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return ContextCompat.getColor(this, R.color.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                return ContextCompat.getColor(this, R.color.permission_status_rationale);
            default:
                return ContextCompat.getColor(this, R.color.permission_status_rationale);
        }
    }

    private void visualiseStatus(TextView view, PermissifyManager.CallRequestStatus status) {
        view.setText(getStatusText(status));
        ((ViewGroup) view.getParent()).setBackgroundColor(getStatusColor(status));
    }
}
