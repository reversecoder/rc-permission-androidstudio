package com.reversecoder.permission.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.onPermissionItemClickListener;
import com.reversecoder.permission.permissify.PermissifyActivity;
import com.reversecoder.permission.permissify.PermissionCallOptions;
import com.reversecoder.permission.permissify.PermissionDeniedInfoDialogFragment;
import com.reversecoder.permission.util.EnumManager;
import com.reversecoder.permission.util.PermissionUtil;
import com.reversecoder.permission.util.SessionManager;

import java.util.ArrayList;

public class PermissionActivity extends PermissifyActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;

    onPermissionItemClickListener permissionItemClickListener = new onPermissionItemClickListener() {
        @Override
        public void getCurrentPermission(ManifestPermission permission) {
//            Toast.makeText(PermissionActivity.this,permission.getFullName()+"\n"+permission.getUuid(),Toast.LENGTH_SHORT).show();

            //call to permissify using without dialogs, but with custom behavior (handled in onCallWithPermissionResult)
            getPermissifyManager().callWithPermission(PermissionActivity.this, permission.getUuid(), permission.getFullName(), new PermissionCallOptions.Builder()
                    .withDefaultDenyDialog(true)
                    .withDefaultRationaleDialog(true)
                    .build());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_permission);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);
        ArrayList<ManifestPermission> data = PermissionUtil.getAllCustomizedPermissions(PermissionActivity.this, "com.reversecoder.permission.demo");
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionActivity.this, data, permissionItemClickListener);
        listViewPermission.setAdapter(permissionListViewAdapter);

//        if(permissionListViewAdapter.isActionTakenForPermissions()){
//            finish();
//        }


    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        ManifestPermission manifestPermission = permissionListViewAdapter.getPermission(callId);
        if (manifestPermission != null) {
//            Toast.makeText(PermissionActivity.this,"My status: "+status.name(),Toast.LENGTH_SHORT).show();
            permissionListViewAdapter.updatePermissionStatus(callId, status);
        }

//        if(permissionListViewAdapter.isActionTakenForPermissions()){
//            finish();
//        }

    }

    public void onResume() {
        super.onResume();

//        if(permissionListViewAdapter !=null){
//            /*
//            * test start here
//            * */
//            ArrayList<ManifestPermission> currentListData=permissionListViewAdapter.getPermissions();
//            ArrayList<ManifestPermission> currentAppData = PermissionUtil.getAllPermissions(PermissionActivity.this, "com.reversecoder.permission.demo");
//
//            for(int i=0;i<currentListData.size();i++){
//                Log.d("currentListData "+currentListData.get(i).getShortName()+" is: ",currentListData.get(i).getPermissionRequestStatus().name());
//            }
//
//            for(int i=0;i<currentAppData.size();i++){
//                Log.d("currentAppData "+currentAppData.get(i).getShortName()+" is: ",currentAppData.get(i).getPermissionRequestStatus().name());
//            }
//
////            for(int i=0;i<currentAppData.size();i++){
////                if(!SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()).equalsIgnoreCase("")){
////                    switch (currentAppData.get(i).getPermissionRequestStatus()){
////                        case UNKNOWN:
////                            permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(),currentAppData.get(i).getPermissionRequestStatus());
////                            break;
////                        case PERMISSION_GRANTED:
////                            permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.PERMISSION_GRANTED);
////                            break;
////                    }
////                }
////            }
//
//
//
//              /*
//            * test end here
//            * */
//            permissionListViewAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS) {
            Toast.makeText(PermissionActivity.this, "Got request code: " + PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS, Toast.LENGTH_SHORT).show();

            ArrayList<ManifestPermission> currentListData = permissionListViewAdapter.getPermissions();
            ArrayList<ManifestPermission> currentAppData = PermissionUtil.getAllPermissions(PermissionActivity.this, "com.reversecoder.permission.demo");

            for (int i = 0; i < currentListData.size(); i++) {
                Log.d("currentListData " + currentListData.get(i).getShortName() + " is: ", currentListData.get(i).getPermissionRequestStatus().name());
            }

            for (int i = 0; i < currentAppData.size(); i++) {
                Log.d("currentAppData " + currentAppData.get(i).getShortName() + " is: ", currentAppData.get(i).getPermissionRequestStatus().name());
            }

            if (permissionListViewAdapter != null) {
                for (int i = 0; i < currentAppData.size(); i++) {
                    if (currentAppData.get(i).getPermissionRequestStatus() != currentListData.get(i).getPermissionRequestStatus()) {
                        switch (currentAppData.get(i).getPermissionRequestStatus()) {
                            case UNKNOWN:
                                    if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.PERMISSION_GRANTED){
                                        onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                    }else if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.UNKNOWN){
                                        onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.UNKNOWN);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.UNKNOWN);
                                    }else if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.PERMISSION_DENIED){
                                        onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.PERMISSION_DENIED);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED);
                                    }else if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.PERMISSION_DENIED_FOREVER){
                                        onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_DENIED_FOREVER);
                                    }else if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.PERMISSION_RATIONALE){
                                        onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.PERMISSION_RATIONALE);
//                                        permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_RATIONALE);
                                    }
                                break;
                            case PERMISSION_GRANTED:
//                                if(EnumManager.getInstance(SessionManager.getStringSetting(PermissionActivity.this, currentAppData.get(i).getFullName()),PermissionRequestStatus.class)== PermissionRequestStatus.UNKNOWN){
//                                    permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
//                                }
//                                permissionListViewAdapter.updatePermissionStatus(permissionListViewAdapter.getPermissions().get(i).getUuid(), PermissionRequestStatus.PERMISSION_GRANTED);
                                onCallWithPermissionResult(permissionListViewAdapter.getPermissions().get(i).getUuid(),PermissionRequestStatus.PERMISSION_GRANTED);
                                break;
                        }
                    }
                }
            }
        }
    }

}