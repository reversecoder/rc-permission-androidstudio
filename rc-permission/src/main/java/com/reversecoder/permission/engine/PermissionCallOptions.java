package com.reversecoder.permission.engine;

import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionCallOptions implements Serializable {

    private boolean showRationaleDialog;
    private String rationaleDialogMsg;
    private int rationaleDialogMsgRes;
    private boolean rationaleEnabled = true;

    private boolean showDenyDialog;
    private String denyDialogMsg;
    private int denyDialogMsgRes;

    boolean showRationaleDialog() {
        return showRationaleDialog;
    }

    public String getRationaleDialogMsg() {
        return rationaleDialogMsg;
    }

    public int getRationaleDialogMsgRes() {
        return rationaleDialogMsgRes;
    }

    boolean showDenyDialog() {
        return showDenyDialog;
    }

    public String getDenyDialogMsg() {
        return denyDialogMsg;
    }

    public int getDenyDialogMsgRes() {
        return denyDialogMsgRes;
    }

    boolean isRationaleEnabled() {
        return rationaleEnabled;
    }

    void setRationaleDialogMsgRes(int rationaleDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
    }

    void setDenyDialogMsgRes(int denyDialogMsgRes) {
        this.denyDialogMsgRes = denyDialogMsgRes;
    }

    public static class Builder {

        private PermissionCallOptions buildObj;

        public Builder() {
            buildObj = new PermissionCallOptions();
        }

        public PermissionCallOptions build() {
            return buildObj;
        }

        public Builder withRationaleDialogMsg(@StringRes int rationaleMsgRes) {
            buildObj.rationaleDialogMsgRes = rationaleMsgRes;
            buildObj.showRationaleDialog = true;

            return this;
        }

        public Builder withRationaleDialogMsg(String rationaleMsg) {
            buildObj.rationaleDialogMsg = rationaleMsg;
            buildObj.showRationaleDialog = true;

            return this;
        }

        public Builder withDefaultRationaleDialog(boolean useDefault) {
            buildObj.showRationaleDialog = useDefault;

            return this;
        }

        public Builder withRationaleEnabled(boolean enabled) {
            buildObj.rationaleEnabled = enabled;

            return this;
        }

        public Builder withDenyDialogMsg(@StringRes int denyDialogMsgRes) {
            buildObj.denyDialogMsgRes = denyDialogMsgRes;
            buildObj.showDenyDialog = true;

            return this;
        }

        public Builder withDenyDialogMsg(String denyDialogMsg) {
            buildObj.denyDialogMsg = denyDialogMsg;
            buildObj.showDenyDialog = true;

            return this;
        }

        public Builder withDefaultDenyDialog(boolean useDefault) {
            buildObj.showDenyDialog = useDefault;

            return this;
        }
    }

}
