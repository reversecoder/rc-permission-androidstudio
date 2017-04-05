package com.reversecoder.permission.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.onPermissionItemClickListener;
import com.reversecoder.permission.permissify.PermissifyActivity;
import com.reversecoder.permission.permissify.PermissionCallOptions;
import com.reversecoder.permission.util.PermissionUtil;

import java.util.ArrayList;

public class PermissionActivity extends PermissifyActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;

    onPermissionItemClickListener permissionItemClickListener = new onPermissionItemClickListener() {
        @Override
        public void getCurrentPermission(ManifestPermission permission) {
//            Toast.makeText(PermissionActivity.this,permission.getName()+"\n"+permission.getUuid(),Toast.LENGTH_SHORT).show();

            //call to permissify using without dialogs, but with custom behavior (handled in onCallWithPermissionResult)
            getPermissifyManager().callWithPermission(PermissionActivity.this, permission.getUuid(), permission.getName(),
                    new PermissionCallOptions.Builder()
                            .withDefaultDenyDialog(false)
                            .withRationaleEnabled(false)
                            .build());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_permission);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);
        ArrayList<ManifestPermission> data = PermissionUtil.getAllPermissions(PermissionActivity.this, "com.reversecoder.permission.demo");
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionActivity.this, data, permissionItemClickListener);
        listViewPermission.setAdapter(permissionListViewAdapter);

        if(permissionListViewAdapter.isActionTakenForPermissions()){
            finish();
        }
    }

    @Override
    public void onCallWithPermissionResult(int callId, PermissionRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        ManifestPermission manifestPermission=permissionListViewAdapter.getPermission(callId);
        if(manifestPermission!=null){
//            Toast.makeText(PermissionActivity.this,"My status: "+status.name(),Toast.LENGTH_SHORT).show();
            permissionListViewAdapter.updatePermissionStatus(callId,status);
        }

        if(permissionListViewAdapter.isActionTakenForPermissions()){
            finish();
        }
    }

    public void onResume(){
        super.onResume();

        if(permissionListViewAdapter !=null){
            permissionListViewAdapter.notifyDataSetChanged();
        }
    }

}


