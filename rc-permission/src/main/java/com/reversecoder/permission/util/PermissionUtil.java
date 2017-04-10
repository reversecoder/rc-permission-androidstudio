package com.reversecoder.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;

import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;

import java.util.ArrayList;
import java.util.Arrays;

import static com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment.REQUEST_CODE_APPLICATION_DETAILS_SETTINGS;

/**
 * @author Md. Rashsadul Alam
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

    public static ArrayList<ManifestPermission> getAllPermissionsWithAutoGranted(Context context, String appPackage) {
        ArrayList<String> permissions;
        ArrayList<ManifestPermission> mPermissions = new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            permissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
            ManifestPermission manifestPermission;
            for (int i = 0; i < permissions.size(); i++) {
                if (isPermissionGranted(context, permissions.get(i))) {
                    manifestPermission = new ManifestPermission(permissions.get(i), PermissionRequestStatus.PERMISSION_GRANTED);
                } else {
                    manifestPermission = new ManifestPermission(permissions.get(i), PermissionRequestStatus.UNKNOWN);
                }
                mPermissions.add(manifestPermission);
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static ArrayList<ManifestPermission> getAllPermissionsWithoutAutoGranted(Context context, String appPackage) {
        ArrayList<String> permissions;
        ArrayList<ManifestPermission> mPermissions = new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            permissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
            ManifestPermission manifestPermission;
            for (int i = 0; i < permissions.size(); i++) {
                if (!isAutoGrantedPermission(permissions.get(i))) {
                    if (isPermissionGranted(context, permissions.get(i))) {
                        manifestPermission = new ManifestPermission(permissions.get(i), PermissionRequestStatus.PERMISSION_GRANTED);
                    } else {
                        manifestPermission = new ManifestPermission(permissions.get(i), PermissionRequestStatus.UNKNOWN);
                    }
                    mPermissions.add(manifestPermission);
                }
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static ArrayList<ManifestPermission> getAllCustomizedPermissions(Context context, String appPackage) {
        ArrayList<String> permissions;
        ArrayList<ManifestPermission> mPermissions = new ArrayList<ManifestPermission>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            permissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
            ManifestPermission manifestPermission;
            for (int i = 0; i < permissions.size(); i++) {
                if (!isAutoGrantedPermission(permissions.get(i))) {
                    if (!SessionManager.getStringSetting(context, permissions.get(i)).equalsIgnoreCase("")) {
                        manifestPermission = new ManifestPermission(permissions.get(i), EnumManager.getInstance(SessionManager.getStringSetting(context, permissions.get(i)), PermissionRequestStatus.class));
                    } else {
                        manifestPermission = new ManifestPermission(permissions.get(i), PermissionRequestStatus.UNKNOWN);
                        SessionManager.setStringSetting(context, permissions.get(i), PermissionRequestStatus.UNKNOWN.name());
                    }
                    mPermissions.add(manifestPermission);
                }
            }
            return mPermissions;
        } catch (Exception e) {
        }
        return null;
    }

    public static int getTargetSdkVersion(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
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

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean isAutoGrantedPermission(String permissionName) {
        for (ManifestPermission d : getAllAutoGrantedPermissionList()) {
            if (d.getFullName().equalsIgnoreCase(permissionName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllPermissionGranted(Context context) {
        ArrayList<ManifestPermission> permissions = getAllPermissionsWithoutAutoGranted(context, getPackageName(context));
        for (ManifestPermission d : permissions) {
            if (d.getPermissionRequestStatus() == PermissionRequestStatus.PERMISSION_GRANTED) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * @param activity    The instance of the activity.
     * @param requestCode If you need any callback from application setting then use any
     *                    positive request code otherwise use -1.
     */
    public static void openApplicationSetting(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (requestCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, REQUEST_CODE_APPLICATION_DETAILS_SETTINGS);
        }
    }

    public static ArrayList<ManifestPermission> getAllAutoGrantedPermissionList() {
        ArrayList<ManifestPermission> manifestPermissions = new ArrayList<ManifestPermission>();

        manifestPermissions.add(new ManifestPermission("android.permission.ACCESS_LOCATION_EXTRA_COMMANDS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.ACCESS_NETWORK_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.ACCESS_NOTIFICATION_POLICY", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.ACCESS_WIFI_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.ACCESS_WIMAX_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.BLUETOOTH", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.BLUETOOTH_ADMIN", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.BROADCAST_STICKY", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.CHANGE_NETWORK_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.CHANGE_WIFI_MULTICAST_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.CHANGE_WIFI_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.CHANGE_WIMAX_STATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.DISABLE_KEYGUARD", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.EXPAND_STATUS_BAR", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.FLASHLIGHT", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.GET_ACCOUNTS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.GET_PACKAGE_SIZE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.INTERNET", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.KILL_BACKGROUND_PROCESSES", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.MODIFY_AUDIO_SETTINGS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.NFC", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.READ_SYNC_SETTINGS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.READ_SYNC_STATS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.RECEIVE_BOOT_COMPLETED", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.REORDER_TASKS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.REQUEST_INSTALL_PACKAGES", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.SET_TIME_ZONE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.SET_WALLPAPER", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.SET_WALLPAPER_HINTS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.SUBSCRIBED_FEEDS_READ", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.TRANSMIT_IR", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.USE_FINGERPRINT", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.VIBRATE", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.WAKE_LOCK", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.WRITE_SYNC_SETTINGS", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("android.permission.READ_LOGS", PermissionRequestStatus.PERMISSION_GRANTED));

        manifestPermissions.add(new ManifestPermission("com.android.alarm.permission.SET_ALARM", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("com.android.launcher.permission.INSTALL_SHORTCUT", PermissionRequestStatus.PERMISSION_GRANTED));
        manifestPermissions.add(new ManifestPermission("com.android.launcher.permission.UNINSTALL_SHORTCUT", PermissionRequestStatus.PERMISSION_GRANTED));

        return manifestPermissions;

    }
}
