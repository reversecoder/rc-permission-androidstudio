package com.reversecoder.permission.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.reversecoder.permission.activity.BasePermissionActivity;
import com.reversecoder.permission.activity.PermissionListActivity;
import com.reversecoder.permission.demo.R;
import com.reversecoder.permission.engine.PermissionCallOptions;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.util.PermissionUtil;

import static com.reversecoder.permission.activity.PermissionListActivity.REQUEST_CODE_PERMISSIONS;
import static com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS;

/**
 * @author Md. Rashsadul Alam
 */
public class MainActivity extends BasePermissionActivity {

    TextView permissionStatus;
    String permissionStatusText = "Checking permissions!!!";
    Button btnPermissionList, btnSinglePermission, btnRemovePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initActions();
    }

    private void initUI() {
        permissionStatus = (TextView) findViewById(R.id.permission_status);
        permissionStatus.setText(permissionStatusText);

        btnPermissionList = (Button) findViewById(R.id.btn_permission_list);
        btnSinglePermission = (Button) findViewById(R.id.btn_single_permission);
        btnRemovePermissions = (Button) findViewById(R.id.btn_remove_permissions);
    }

    private void initActions() {
        btnPermissionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(MainActivity.this, PermissionListActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
                } else {
                    Toast.makeText(MainActivity.this, "Permissions are granted by default under MarshMallow", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSinglePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.isPermissionGranted(MainActivity.this, Manifest.permission.CAMERA)) {
                        Toast.makeText(MainActivity.this, "Please remove camera permission first!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        ManifestPermission manifestPermission = new ManifestPermission(Manifest.permission.CAMERA, PermissionRequestStatus.UNKNOWN);
                        getPermissionManager().callWithPermission(MainActivity.this, manifestPermission.getUuid(), manifestPermission.getFullName(), new PermissionCallOptions.Builder()
                                .withDefaultDenyDialog(true)
                                .withDefaultRationaleDialog(true)
                                .build());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permissions are granted by default under MarshMallow", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRemovePermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionUtil.openApplicationSetting(MainActivity.this, REQUEST_CODE_APPLICATION_DETAILS_SETTINGS);
                } else {
                    Toast.makeText(MainActivity.this, "This feature is not available under MarshMallow", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (resultCode == RESULT_OK) {
                permissionStatusText = "All Permissions accepted. Please remove permission for checking this feature again.";
            } else if (resultCode == RESULT_CANCELED) {
                permissionStatusText = "Permissions granting process canceled!!!";
            }

            permissionStatus.setText(permissionStatusText);
        } else if (requestCode == REQUEST_CODE_APPLICATION_DETAILS_SETTINGS) {
            Toast.makeText(MainActivity.this, "Returned from application detail settings.", Toast.LENGTH_SHORT).show();
        }
    }
}
