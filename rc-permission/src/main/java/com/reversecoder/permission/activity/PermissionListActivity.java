package com.reversecoder.permission.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment;
import com.reversecoder.permission.engine.PermissionCallOptions;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.onPermissionItemClickListener;
import com.reversecoder.permission.util.EnumManager;
import com.reversecoder.permission.util.PermissionUtil;

import java.util.ArrayList;

import static com.reversecoder.permission.util.PermissionUtil.PERMISSION_DRAW_OVER_OTHER_APPS;
import static com.reversecoder.permission.util.PermissionUtil.getStringSetting;
import static com.reversecoder.permission.util.PermissionUtil.setStringSetting;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionListActivity extends BasePermissionActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;
    public static final int REQUEST_CODE_PERMISSIONS = 42000;
    public static final int REQUEST_CODE_DRAW_OVER_OTHER_APPS = 48000;

    onPermissionItemClickListener permissionItemClickListener = new onPermissionItemClickListener() {
        @Override
        public void getCurrentPermission(ManifestPermission permission) {
            getPermissionManager().callWithPermission(PermissionListActivity.this, permission.getUuid(), permission.getFullName(), new PermissionCallOptions.Builder()
                    .withDefaultDenyDialog(true)
                    .withDefaultRationaleDialog(true)
                    .build());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_list);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);
        ArrayList<ManifestPermission> data = PermissionUtil.getAllCustomizedPermissions(PermissionListActivity.this, PermissionUtil.getPackageName(PermissionListActivity.this));
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionListActivity.this, data, permissionItemClickListener);
        listViewPermission.setAdapter(permissionListViewAdapter);

        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        super.onBackPressed();
    }

    public void exitOnPermissionGranted() {
        if (permissionListViewAdapter != null && permissionListViewAdapter.isAllPermissionGranted()) {
            if (getStringSetting(PermissionListActivity.this, PERMISSION_DRAW_OVER_OTHER_APPS) != null
                    && getStringSetting(PermissionListActivity.this, PERMISSION_DRAW_OVER_OTHER_APPS).equalsIgnoreCase(PermissionRequestStatus.UNKNOWN.name())
                    && !Settings.canDrawOverlays(PermissionListActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_DRAW_OVER_OTHER_APPS);
            } else {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

//    @Override
//    public void finish() {
//        if (permissionListViewAdapter != null && permissionListViewAdapter.isAllPermissionGranted()){
//            if(getStringSetting(PermissionListActivity.this, PERMISSION_DRAW_OVER_OTHER_APPS)!=null){
//
//            }else{
//                super.finish();
//            }
//        }else{
//            super.finish();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS) {
            ArrayList<ManifestPermission> currentListData = permissionListViewAdapter.getPermissions();
            ArrayList<ManifestPermission> currentAppData = PermissionUtil.getAllPermissionsWithoutAutoGranted(PermissionListActivity.this, PermissionUtil.getPackageName(PermissionListActivity.this));

            if (permissionListViewAdapter != null) {
                for (int i = 0; i < currentAppData.size(); i++) {
                    if (currentAppData.get(i).getPermissionRequestStatus() != currentListData.get(i).getPermissionRequestStatus()) {
                        switch (currentAppData.get(i).getPermissionRequestStatus()) {
                            case UNKNOWN:
                                if (EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_GRANTED) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                } else if (EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.UNKNOWN) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                } else if (EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_DENIED) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED);
                                } else if (EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_DENIED_FOREVER) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
                                } else if (EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()), PermissionRequestStatus.class) == PermissionRequestStatus.PERMISSION_RATIONALE) {
                                    onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_RATIONALE);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_RATIONALE);
                                }
                                break;
                            case PERMISSION_GRANTED:
//                                if(EnumManager.getInstance(getStringSetting(PermissionListActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.UNKNOWN){
//                                    permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
//                                }
//                                permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
                                onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
                                break;
                        }
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_DRAW_OVER_OTHER_APPS) {
            if (Settings.canDrawOverlays(PermissionListActivity.this)) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                setStringSetting(PermissionListActivity.this, PERMISSION_DRAW_OVER_OTHER_APPS, PermissionRequestStatus.PERMISSION_GRANTED.name());
                Toast.makeText(this,
                        "Draw over other app permission is granted.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

}
