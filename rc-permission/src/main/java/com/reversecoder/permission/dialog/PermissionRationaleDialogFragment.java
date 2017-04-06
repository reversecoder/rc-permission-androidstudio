package com.reversecoder.permission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.reversecoder.permission.R;
import com.reversecoder.permission.activity.BasePermissionActivity;
import com.reversecoder.permission.engine.PermissionCallOptions;
import com.reversecoder.permission.engine.PermissionConfig;
import com.reversecoder.permission.engine.PermissionManager;
import com.reversecoder.permission.model.AlertDialogFactory;

/**
 * @author Md. Rashsadul Alam
 */
public class PermissionRationaleDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG = "rc-permission-handler";
    private static final String ARG_PENDING_CALL = "pendingCall";

    private PermissionManager.PendingPermissionCall pendingCall;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pendingCall = (PermissionManager.PendingPermissionCall) getArguments().getSerializable(ARG_PENDING_CALL);

        return PermissionConfig.get()
                .getRationaleDialogFactory()
                .createDialog(getContext(), getDialogMessage(pendingCall.options), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        BasePermissionActivity activity = (BasePermissionActivity) getActivity();

        activity.getPermissionManager().getLifecycleHandler().onRationaleDialogConfirm(pendingCall);
    }

    public static void showDialog(FragmentManager fragmentManager, PermissionManager.PendingPermissionCall pendingPermissionCall) {
        if (fragmentManager.findFragmentByTag(TAG) != null) {
            Log.w(TAG, "Dialog is already on screen - rejecting show command");
            return;
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_PENDING_CALL, pendingPermissionCall);

        PermissionRationaleDialogFragment dialog = new PermissionRationaleDialogFragment();
        dialog.setArguments(args);
        dialog.setCancelable(false);

        fragmentManager
                .beginTransaction()
                .add(dialog, TAG)
                .commitAllowingStateLoss();
    }

    private String getDialogMessage(PermissionCallOptions callOptions) {
        return callOptions.getRationaleDialogMsg() != null ? callOptions.getRationaleDialogMsg() : getString(callOptions.getRationaleDialogMsgRes());
    }

    public static AlertDialogFactory getDefaultDialogFactory() {
        return new AlertDialogFactory() {
            @Override
            public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
                return new AlertDialog.Builder(context)
                        .setTitle(R.string.permissify_permission_rationale_title)
                        .setMessage(dialogMsg)
                        .setPositiveButton(android.R.string.ok, onClickListener)
                        .create();
            }
        };
    }
}
