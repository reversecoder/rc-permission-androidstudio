package com.reversecoder.permission.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment;
import com.reversecoder.permission.engine.PermissionCallOptions;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.onPermissionItemClickListener;
import com.reversecoder.permission.util.EnumManager;
import com.reversecoder.permission.util.PermissionUtil;
import com.reversecoder.permission.util.SessionManager;

import java.util.ArrayList;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionActivity extends BasePermissionActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;
    public static final int REQUEST_CODE_PERMISSION_GRANTED = 42000;

    onPermissionItemClickListener permissionItemClickListener = new onPermissionItemClickListener() {
        @Override
        public void getCurrentPermission(ManifestPermission permission) {
            getPermissionManager().callWithPermission(PermissionActivity.this, permission.getUuid(), permission.getFullName(), new PermissionCallOptions.Builder()
                    .withDefaultDenyDialog(true)
                    .withDefaultRationaleDialog(true)
                    .build());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);

        ArrayList<ManifestPermission> data = PermissionUtil.getAllCustomizedPermissions(PermissionActivity.this, PermissionUtil.getPackageName(PermissionActivity.this));
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionActivity.this, data, permissionItemClickListener);
        listViewPermission.setAdapter(permissionListViewAdapter);
    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        ManifestPermission manifestPermission = permissionListViewAdapter.getPermission(callId);
        if (manifestPermission != null) {
            permissionListViewAdapter.updatePermissionStatus(callId, status);
        }

        exitOnPermissionGranted();
    }

    @Override
    public void onResume() {
        super.onResume();
        exitOnPermissionGranted();
    }

    public void exitOnPermissionGranted() {
        if (permissionListViewAdapter != null) {
            if (permissionListViewAdapter.isAllPermissionGranted()) {
                finish();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS) {
            ArrayList<ManifestPermission> currentListData = permissionListViewAdapter.getPermissions();
            ArrayList<ManifestPermission> currentAppData = PermissionUtil.getAllPermissionsWithoutAutoGranted(PermissionActivity.this, PermissionUtil.getPackageName(PermissionActivity.this));

            if (permissionListViewAdapter != null) {
                for (int i = 0; i < currentAppData.size(); i++) {
                    if (currentAppData.get(i).getPermissionRequestStatus() != currentListData.get(i).getPermissionRequestStatus()) {
                        switch (currentAppData.get(i).getPermissionRequestStatus()) {
                            case UNKNOWN:
                                if (EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_GRANTED) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                } else if (EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.UNKNOWN) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                } else if (EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_DENIED) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED);
                                } else if (EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_DENIED_FOREVER) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
                                } else if (EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_RATIONALE) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_RATIONALE);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_RATIONALE);
                                }
                                break;
                            case PERMISSION_GRANTED:
//                                if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.UNKNOWN){
//                                    permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
//                                }
//                                permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
                                onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
                                break;
                        }
                    }
                }
            }
        }
    }

}