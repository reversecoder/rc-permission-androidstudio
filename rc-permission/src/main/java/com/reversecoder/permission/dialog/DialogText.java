package com.reversecoder.permission.dialog;

import android.support.annotation.StringRes;

/**
 * @author Md. Rashsadul Alam
 */
public class DialogText {

    public final int rationaleDialogMsgRes;
    public final int denyDialogMsgRes;

    public DialogText(@StringRes int rationaleDialogMsgRes, @StringRes int denyDialogMsgRes) {
        this.rationaleDialogMsgRes = rationaleDialogMsgRes;
        this.denyDialogMsgRes = denyDialogMsgRes;
    }
}
