package com.reversecoder.permission.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * @author Md. Rashsadul Alam
 */
public interface AlertDialogFactory {
    AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener);
}