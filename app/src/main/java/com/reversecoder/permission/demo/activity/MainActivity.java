package com.reversecoder.permission.demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.permission.activity.PermissionActivity;
import com.reversecoder.permission.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            startActivity(intent);
        }
    }
}
