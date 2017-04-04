package com.reversecoder.permission.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reversecoder.permission.R;
import com.reversecoder.permission.activity.PermissionActivity;
import com.reversecoder.permission.model.ManifestPermission;

import java.util.ArrayList;

/**
 * Created by rashed on 4/4/17.
 */

public class PermissionListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ManifestPermission> mData;
    private static LayoutInflater inflater = null;

    public PermissionListViewAdapter(Context context, ArrayList<ManifestPermission> data) {
        mContext = context;
        mData = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mData.size();
    }

    public ManifestPermission getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_permission, null);
        TextView tvPermissionName = (TextView) vi.findViewById(R.id.tv_permission_name);
        TextView tvPermissionStatus = (TextView) vi.findViewById(R.id.tv_permission_status);
        updateRowView(tvPermissionName, tvPermissionStatus, vi, position);
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PermissionActivity) mContext).setContactPermission();
            }
        });
        return vi;
    }

    public void updateRowView(TextView permissionName, TextView statusText, View rowView, int position) {
        rowView.setBackgroundColor(getStatusColor(position));
        statusText.setText(getStatusText(position));
        permissionName.setText(getItem(position).getName().replace("android.permission.",""));
    }


    public String getStatusText(int position) {
        String statusText = "";
        switch (getItem(position).getPermissionRequestStatus()) {
            case PERMISSION_GRANTED:
                statusText = mContext.getString(R.string.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                statusText = mContext.getString(R.string.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                statusText = mContext.getString(R.string.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                statusText = mContext.getString(R.string.permission_status_rationale);
        }
        return statusText;
    }

    @ColorInt
    public int getStatusColor(int position) {
        switch (getItem(position).getPermissionRequestStatus()) {
            case PERMISSION_GRANTED:
                return ContextCompat.getColor(mContext, R.color.permission_status_granted);
            case PERMISSION_DENIED_ONCE:
                return ContextCompat.getColor(mContext, R.color.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return ContextCompat.getColor(mContext, R.color.permission_status_denied_forever);
            case SHOW_PERMISSION_RATIONALE:
                return ContextCompat.getColor(mContext, R.color.permission_status_rationale);
            default:
                return ContextCompat.getColor(mContext, R.color.permission_status_rationale);
        }
    }
}