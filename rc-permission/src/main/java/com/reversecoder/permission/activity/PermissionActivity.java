package com.reversecoder.permission.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.adapter.PermissionListViewAdapter;
import com.reversecoder.permission.util.PermissionUtil;

public class PermissionActivity extends AppCompatActivity {

    ListView listViewPermission;
    PermissionListViewAdapter permissionListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_permission);

        listViewPermission = (ListView) findViewById(R.id.listview_permission);
        permissionListViewAdapter = new PermissionListViewAdapter(PermissionActivity.this, PermissionUtil.getAllPermissions(PermissionActivity.this, "com.reversecoder.permission.demo"));
        listViewPermission.setAdapter(permissionListViewAdapter);
    }
}
