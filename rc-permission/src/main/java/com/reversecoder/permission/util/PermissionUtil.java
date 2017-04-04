package com.reversecoder.permission.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.reversecoder.permission.model.ManifestPermission;

import java.util.ArrayList;
import java.util.Arrays;

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
                    manifestPermission = new ManifestPermission(pi.requestedPermissions[i],true);
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
                manifestPermission = new ManifestPermission(permissions.get(i),isPermissionGranted(context,permissions.get(i)));
                mPermissions.add(manifestPermission);
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean hasPermission(Context context, String permission) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        try {
            if ((ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)) {
                return true;
            }

        } catch (Exception e) {
        }
        return false;
    }
}
