package com.reversecoder.permission.demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.reversecoder.permission.activity.PermissionActivity;
import com.reversecoder.permission.demo.R;
import com.reversecoder.permission.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    TextView permissionStatus;
    String permissionStatusText="Checking permissions!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionStatus= (TextView)findViewById(R.id.permission_status);
        permissionStatus.setText(permissionStatusText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            startActivityForResult(intent, PermissionActivity.REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionActivity.REQUEST_CODE_PERMISSIONS) {
            if( resultCode == RESULT_OK){
                permissionStatusText="Permissions accepted!!!";
            } else if(resultCode == RESULT_CANCELED){
                permissionStatusText="Permissions canceled!!!";
            }

            permissionStatus.setText(permissionStatusText);
        }
    }
}
