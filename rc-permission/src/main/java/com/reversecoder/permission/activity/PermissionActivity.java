package com.reversecoder.permission.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.permissify.PermissifyActivity;
import com.reversecoder.permission.permissify.PermissifyManager;
import com.reversecoder.permission.permissify.PermissionCallOptions;
import com.reversecoder.permission.util.PermissionUtil;

public class PermissionActivity extends PermissifyActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;

    public static final int CONTACTS_PERMISSION_REQUEST_ID = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_permission);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionActivity.this, PermissionUtil.getAllPermissions(PermissionActivity.this, "com.reversecoder.permission.demo"));
        listViewPermission.setAdapter(permissionListViewAdapter);
    }

    public void setContactPermission(){
        //call to permissify using without dialogs, but with custom behavior (handled in onCallWithPermissionResult)
        getPermissifyManager().callWithPermission(PermissionActivity.this, CONTACTS_PERMISSION_REQUEST_ID, Manifest.permission.READ_CONTACTS,
                new PermissionCallOptions.Builder()
                        .withDefaultDenyDialog(false)
                        .withRationaleEnabled(false)
                        .build());
    }

    public void refreshListView(){
        permissionListViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

//        if (callId == LOCATION_PERMISSION_REQUEST_ID) {
//            visualiseStatus(locationStatus, status);
//
//            switch (status) {
//                case PERMISSION_GRANTED:
//                    //getUserLocation();
//                    break;
//                case PERMISSION_DENIED_ONCE:
//                    //do some custom logic
//                    break;
//                case PERMISSION_DENIED_FOREVER:
//                    //do some custom logic
//                case SHOW_PERMISSION_RATIONALE:
//                    //do some custom logic
//            }
//        } else if (callId == CAMERA_PERMISSION_REQUEST_ID) {
//            visualiseStatus(cameraStatus, status);
//
//            if (status == PermissifyManager.CallRequestStatus.SHOW_PERMISSION_RATIONALE) {
//                showCameraRationaleSnackbar(callId);
//            }
//
//        } else

            if (callId == CONTACTS_PERMISSION_REQUEST_ID) {
//                visualiseStatus(contactsStatus, status);

                permissionListViewAdapter.getItem(0).setPermissionRequestStatus(status);

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

                refreshListView();

//                contactsButton.setEnabled(status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_ONCE && status != PermissifyManager.CallRequestStatus.PERMISSION_DENIED_FOREVER);
            }

        }
}


