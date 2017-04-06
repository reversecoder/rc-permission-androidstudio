package com.reversecoder.permission.engine;

import com.reversecoder.permission.R;
import com.reversecoder.permission.dialog.DialogText;
import com.reversecoder.permission.dialog.PermissionDeniedInfoDialogFragment;
import com.reversecoder.permission.dialog.PermissionRationaleDialogFragment;
import com.reversecoder.permission.model.AlertDialogFactory;

import java.util.HashMap;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionConfig {

    private static PermissionConfig sInstance;

    private PermissionCallOptions defaultPermissionCallOptions;
    private HashMap<String, DialogText> defaultTextForPermissions;
    private DialogText permissionTextFallback;
    private AlertDialogFactory rationaleDialogFactory;
    private AlertDialogFactory denyDialogFactory;

    public static PermissionConfig get() {
        if (sInstance == null) {
            throw new RuntimeException("PermissionConfig is not initialized");
        }

        return sInstance;
    }

    private PermissionConfig() {
    }

    public static class Builder {

        private PermissionConfig instance = new PermissionConfig();

        public Builder withDefaultPermissionCallOptions(PermissionCallOptions callOptions) {
            instance.defaultPermissionCallOptions = callOptions;
            return this;
        }

        public Builder withDefaultTextForPermissions(HashMap<String, DialogText> wording) {
            instance.defaultTextForPermissions = wording;
            return this;
        }

        public Builder withPermissionTextFallback(DialogText dialogText) {
            instance.permissionTextFallback = dialogText;
            return this;
        }

        public Builder withDialogRationaleDialogFactory(AlertDialogFactory factory) {
            instance.rationaleDialogFactory = factory;
            return this;
        }

        public Builder withDenyDialogFactory(AlertDialogFactory factory) {
            instance.denyDialogFactory = factory;
            return this;
        }

        public PermissionConfig build() {
            if (instance.denyDialogFactory == null) {
                instance.denyDialogFactory = PermissionDeniedInfoDialogFragment.getDefaultDialogFactory();
            }

            if (instance.rationaleDialogFactory == null) {
                instance.rationaleDialogFactory = PermissionRationaleDialogFragment.getDefaultDialogFactory();
            }

            if (instance.defaultPermissionCallOptions == null) {
                instance.defaultPermissionCallOptions = new PermissionCallOptions.Builder()
                        .withDefaultDenyDialog(true)
                        .withDefaultRationaleDialog(true)
                        .build();
            }

            if (instance.permissionTextFallback == null) {
                instance.permissionTextFallback = new DialogText(R.string.permissify_no_text_fallback, R.string.permissify_no_text_fallback);
            }

            return instance;
        }
    }

    public static void initDefault(PermissionConfig permissionConfig) {
        sInstance = permissionConfig;
    }

    PermissionCallOptions getDefaultPermissionCallOptions() {
        return defaultPermissionCallOptions;
    }

    DialogText getPermissionTextFallback() {
        return permissionTextFallback;
    }

    HashMap<String, DialogText> getDefaultTextForPermissions() {
        return defaultTextForPermissions;
    }

    public AlertDialogFactory getRationaleDialogFactory() {
        return rationaleDialogFactory;
    }

    public AlertDialogFactory getDenyDialogFactory() {
        return denyDialogFactory;
    }

}
