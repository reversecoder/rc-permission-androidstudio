package com.reversecoder.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.reversecoder.permission.activity.PermissionActivity;
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by rashed on 4/4/17.
 */

public class PermissionUtil {

    public static ArrayList<ManifestPermission> getAllGrantedPermissions(Context context, String appPackage) {
        ArrayList<ManifestPermission> granted = new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            ManifestPermission manifestPermission;
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    manifestPermission = new ManifestPermission(pi.requestedPermissions[i], PermissionRequestStatus.PERMISSION_GRANTED);
                    granted.add(manifestPermission);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }

    public static ArrayList<ManifestPermission> getAllPermissions(Context context, String appPackage) {
        ArrayList<String> permissions;
        ArrayList<ManifestPermission> mPermissions=new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            permissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
            ManifestPermission manifestPermission;
            for(int i=0;i<permissions.size();i++){
                if(isPermissionGranted(context,permissions.get(i))){
                    manifestPermission = new ManifestPermission(permissions.get(i),PermissionRequestStatus.PERMISSION_GRANTED);
                }else{
                    manifestPermission = new ManifestPermission(permissions.get(i),PermissionRequestStatus.UNKNOWN);
                }
                mPermissions.add(manifestPermission);
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static ArrayList<ManifestPermission> getAllCustomizedPermissions(Context context, String appPackage) {
        ArrayList<String> permissions;
        ArrayList<ManifestPermission> mPermissions=new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            permissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
            ManifestPermission manifestPermission;
            for(int i=0;i<permissions.size();i++){
                if(!SessionManager.getStringSetting(context,permissions.get(i)).equalsIgnoreCase("")){
                    manifestPermission = new ManifestPermission(permissions.get(i),EnumManager.getInstance(SessionManager.getStringSetting(context,permissions.get(i)),PermissionRequestStatus.class));
                }else{
                    manifestPermission = new ManifestPermission(permissions.get(i),PermissionRequestStatus.UNKNOWN);
                    SessionManager.setStringSetting(context,permissions.get(i),PermissionRequestStatus.UNKNOWN.name());
                }
                mPermissions.add(manifestPermission);
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static int getTargetSdkVersion(Context context){
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isPermissionGranted(Context context,String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }
}
