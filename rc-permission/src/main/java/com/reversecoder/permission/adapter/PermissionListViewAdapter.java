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
import com.reversecoder.permission.model.ManifestPermission;
import com.reversecoder.permission.model.PermissionRequestStatus;
import com.reversecoder.permission.model.onPermissionItemClickListener;
import com.reversecoder.permission.util.SessionManager;

import java.util.ArrayList;

/**
 * Created by rashed on 4/4/17.
 */

public class PermissionListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ManifestPermission> mData;
    private static LayoutInflater inflater = null;
    private onPermissionItemClickListener mPermissionItemClickListener;

    public void setPermissionItemClickListener(onPermissionItemClickListener permissionItemClickListener) {
        this.mPermissionItemClickListener = permissionItemClickListener;
    }

    public PermissionListViewAdapter(Context context, ArrayList<ManifestPermission> data, onPermissionItemClickListener permissionItemClickListener) {
        mContext = context;
        mData = data;
        this.mPermissionItemClickListener = permissionItemClickListener;
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

    public ArrayList<ManifestPermission> getPermissions(){
        return mData;
    }

    public void setPermissions(ArrayList<ManifestPermission> permissions){
        mData=permissions;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_row_permission, null);
        }
        TextView tvPermissionName = (TextView) vi.findViewById(R.id.tv_permission_name);
        TextView tvPermissionStatus = (TextView) vi.findViewById(R.id.tv_permission_status);
        updateRowView(tvPermissionName, tvPermissionStatus, vi, position);
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionItemClickListener.getCurrentPermission(getItem(position));
            }
        });
        return vi;
    }

    public void updateRowView(TextView permissionName, TextView statusText, View rowView, int position) {
        rowView.setBackgroundColor(getStatusColor(position));
        statusText.setText(getStatusText(position));
        permissionName.setText(getItem(position).getShortName());
    }


    public String getStatusText(int position) {
        switch (getItem(position).getPermissionRequestStatus()) {
            case UNKNOWN:
                return mContext.getString(R.string.permission_status_unknown);
            case PERMISSION_GRANTED:
                return mContext.getString(R.string.permission_status_granted);
            case PERMISSION_DENIED:
                return mContext.getString(R.string.permission_status_denied);
            case PERMISSION_DENIED_FOREVER:
                return mContext.getString(R.string.permission_status_denied_forever);
            case PERMISSION_RATIONALE:
                return mContext.getString(R.string.permission_status_rationale);
            default:
                return mContext.getString(R.string.permission_status_unknown);
        }
    }

    @ColorInt
    public int getStatusColor(int position) {
        switch (getItem(position).getPermissionRequestStatus()) {
            case UNKNOWN:
                return ContextCompat.getColor(mContext, R.color.permission_request_is_not_sent);
            case PERMISSION_GRANTED:
                return ContextCompat.getColor(mContext, R.color.permission_status_granted);
            case PERMISSION_DENIED:
                return ContextCompat.getColor(mContext, R.color.permission_status_denied_once);
            case PERMISSION_DENIED_FOREVER:
                return ContextCompat.getColor(mContext, R.color.permission_status_denied_forever);
            case PERMISSION_RATIONALE:
                return ContextCompat.getColor(mContext, R.color.permission_status_rationale);
        }
        return ContextCompat.getColor(mContext, R.color.permission_request_is_not_sent);
    }

    public ManifestPermission getPermission(int uuid){
        for(ManifestPermission d : mData){
            if(d.getUuid()==uuid){
                return d;
            }
        }
        return null;
    }

    public int getPermissionPosition(int uuid){
        for(int i=0;i<mData.size();i++){
            if(mData.get(i).getUuid()==uuid){
                return i;
            }
        }
        return -1;
    }

    public ManifestPermission updatePermissionStatus(int uuid, PermissionRequestStatus permissionRequestStatus){
        if(getPermission(uuid)!=null){
            mData.get(getPermissionPosition(uuid)).setPermissionRequestStatus(permissionRequestStatus);
            ManifestPermission permission=mData.get(getPermissionPosition(uuid));
            SessionManager.setStringSetting(mContext,permission.getFullName(),permissionRequestStatus.name());
            notifyDataSetChanged();
            return permission;
        }
        return null;
    }

    public boolean isActionTakenForPermissions(){
        for(ManifestPermission d : mData){
            if(d.getPermissionRequestStatus()!= PermissionRequestStatus.UNKNOWN){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }
}